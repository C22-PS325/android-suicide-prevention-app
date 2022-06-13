package com.example.suicidepreventiveapp.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.data.remote.response.ImagePredictionResponse
import com.example.suicidepreventiveapp.data.remote.retrofit.ApiConfig
import com.example.suicidepreventiveapp.databinding.FragmentSelfieBinding
import com.example.suicidepreventiveapp.ui.activity.CameraActivity
import com.example.suicidepreventiveapp.ui.custom.LoadingDialogBar
import com.example.suicidepreventiveapp.utils.createCustomTempFile
import com.example.suicidepreventiveapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SelfieFragment : Fragment() {

    private var _binding: FragmentSelfieBinding? = null
    private val binding get() = _binding!!

    private var getFile: File? = null

    private lateinit var mUserPreferences: UserPreferences
    private lateinit var userModel: UserModel

    private lateinit var loadingDialogBar: LoadingDialogBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSelfieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserPreferences = UserPreferences(requireContext())

        loadingDialogBar = LoadingDialogBar(requireContext())

        binding.btnTakeSelfie.setOnClickListener {
            if (!allPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                openCamera()
            }
        }

        binding.buttonSave.setOnClickListener { uploadPhoto() }
    }

    private fun openCamera() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun uploadPhoto() {
        loadingDialogBar.showDialog("Mohon Tunggu...")
        when {
            getFile == null -> {
                loadingDialogBar.hideDialog()
                Toast.makeText(requireContext(), "Silahkan foto terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

            else -> {
                userModel = mUserPreferences.getUser()

                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    requestImageFile
                )

                val service = ApiConfig.getApiService().imagePrediction(imageMultipart, "Bearer ${userModel.accessToken}")
                service.enqueue(object : Callback<ImagePredictionResponse> {
                    override fun onResponse(
                        call: Call<ImagePredictionResponse>,
                        response: Response<ImagePredictionResponse>
                    ) {
                        loadingDialogBar.hideDialog()
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), (response.body()?.response?.get(0))!!, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ImagePredictionResponse>, t: Throwable) {
                        loadingDialogBar.hideDialog()
                        Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }

    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    requireContext(),
                    "tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            getFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = BitmapFactory.decodeFile(getFile!!.path)

            binding.selfieResult.setImageBitmap(result)
        }
    }

    companion object {

        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val RESULT_CODE = 110

    }
}