package com.example.suicidepreventiveapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.databinding.FragmentQuestionnaire2Binding

class Questionnaire2Fragment : Fragment() {
    private var _binding: FragmentQuestionnaire2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaire2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener{
            val questionnaire3Fragment = Questionnaire3Fragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment_activity_main, questionnaire3Fragment, Questionnaire2Fragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}