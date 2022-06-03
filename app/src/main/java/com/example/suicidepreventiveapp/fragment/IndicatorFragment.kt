package com.example.suicidepreventiveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.suicidepreventiveapp.databinding.FragmentIndicatorBinding

class IndicatorFragment : Fragment() {

    private var _binding: FragmentIndicatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel = ViewModelProvider(this).get(IndicatorViewModel::class.java)

        _binding = FragmentIndicatorBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}