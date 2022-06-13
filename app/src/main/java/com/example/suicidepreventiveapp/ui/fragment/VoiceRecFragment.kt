package com.example.suicidepreventiveapp.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.data.remote.response.VoicePredictionResponse
import com.example.suicidepreventiveapp.data.remote.retrofit.ApiConfig
import com.example.suicidepreventiveapp.databinding.FragmentVoiceRecBinding
import com.example.suicidepreventiveapp.ui.custom.LoadingDialogBar
import com.example.suicidepreventiveapp.utils.createFile
import com.example.suicidepreventiveapp.utils.createFileAudio
import com.example.suicidepreventiveapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.lang.StringBuilder

class VoiceRecFragment : Fragment() {

    private var _binding: FragmentVoiceRecBinding? = null
    private val binding get() = _binding!!

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    private lateinit var getFile: File

    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    private lateinit var mUserPreferences: UserPreferences
    private lateinit var userModel: UserModel

    private lateinit var loadingDialogBar: LoadingDialogBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVoiceRecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAudio()
        mUserPreferences = UserPreferences(requireContext())
        loadingDialogBar = LoadingDialogBar(requireContext())

        binding.btnStartSound.setOnClickListener {
            if (!allPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    VoiceRecFragment.REQUIRED_PERMISSIONS,
                    VoiceRecFragment.REQUEST_CODE_PERMISSIONS
                )
            } else {
                startRecording()
            }
        }

        binding.btnStopSound.setOnClickListener { stopRecording() }

        binding.buttonPlayAudio.setOnClickListener {
            Log.d("VOICE FRAGMENT", output.toString())
            if (output != null) {
                playAudio()
            }
        }

        binding.buttonSave.setOnClickListener { uploadAudio() }
    }

    private fun playAudio() {
        try {
            mMediaPlayer?.setDataSource(requireContext(), Uri.parse(output))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnErrorListener { _, _, _ -> false }
    }

    private fun initAudio() {
        mMediaPlayer = MediaPlayer()
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mMediaPlayer?.setAudioAttributes(attribute)


    }

    private fun startRecording() {
        Toast.makeText(requireContext(), "Rekaman belim mulai!", Toast.LENGTH_SHORT).show()
        try {

            val audioName = CreateRandomAudioFileName(10)

            val audioFile = createFileAudio(requireActivity().application, audioName)
            getFile = audioFile
            output = audioFile.absolutePath
            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            binding.btnStartSound.isEnabled = false
            Toast.makeText(requireContext(), "Rekaman Dimulai!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            binding.btnStartSound.isEnabled = true
            state = false
            Toast.makeText(requireContext(), "Berhenti Merekam", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), "Kamu belum mulai merekam", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionGranted() = VoiceRecFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun CreateRandomAudioFileName(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

    private fun uploadAudio() {
        loadingDialogBar.showDialog("Mohon Tunggu...")

        when {
            output == null -> {
                loadingDialogBar.hideDialog()
                Toast.makeText(requireContext(), "Silahkan Rekam Audio terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

            else -> {
                userModel = mUserPreferences.getUser()

                val requestAudioFile = getFile.asRequestBody("audio/wav".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    getFile.name,
                    requestAudioFile
                )

                val service = ApiConfig.getApiService().voicePrediction(imageMultipart, "Bearer ${userModel.accessToken}")
                service.enqueue(object : Callback<VoicePredictionResponse> {
                    override fun onResponse(
                        call: Call<VoicePredictionResponse>,
                        response: Response<VoicePredictionResponse>
                    ) {
                        loadingDialogBar.hideDialog()
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), response.body()?.response, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<VoicePredictionResponse>, t: Throwable) {
                        loadingDialogBar.hideDialog()
                        Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == VoiceRecFragment.REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    requireContext(),
                    "tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.RECORD_AUDIO)
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val STRING_LENGTH = 5
    }
}