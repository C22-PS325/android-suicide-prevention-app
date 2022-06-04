package com.example.suicidepreventiveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.databinding.FragmentQuestionnaireBinding

class QuestionnaireFragment : Fragment() {
    private var _binding: FragmentQuestionnaireBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener{
            val questionnaire2Fragment = Questionnaire2Fragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment_activity_main, questionnaire2Fragment, Questionnaire2Fragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}