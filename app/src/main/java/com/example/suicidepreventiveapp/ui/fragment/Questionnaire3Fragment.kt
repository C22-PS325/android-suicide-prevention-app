package com.example.suicidepreventiveapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.databinding.FragmentQuestionnaire3Binding

class Questionnaire3Fragment : Fragment() {
    private var _binding: FragmentQuestionnaire3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionnaire3Binding.inflate(inflater, container, false)
        return binding.root
    }

}