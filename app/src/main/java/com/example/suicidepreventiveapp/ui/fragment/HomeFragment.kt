package com.example.suicidepreventiveapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences
    private lateinit var userData: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(view.context)
        userData = userPreferences.getUser()

        binding.username.text = userData.username

        binding.cvQuestionnaire.setOnClickListener {
            val questionnaireFragment = QuestionnaireFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(
                    R.id.nav_host_fragment_activity_main,
                    questionnaireFragment,
                    QuestionnaireFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }

        binding.cvSelfie.setOnClickListener {
            val selfieFragment = SelfieFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(
                    R.id.nav_host_fragment_activity_main,
                    selfieFragment,
                    SelfieFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }

        binding.cvVoiceRec.setOnClickListener {
            val voiceRecFragment = VoiceRecFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment_activity_main, voiceRecFragment, VoiceRecFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}