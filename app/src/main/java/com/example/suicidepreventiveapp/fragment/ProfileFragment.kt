package com.example.suicidepreventiveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnLogout.setOnClickListener {
//            val loginFragment = LoginFragment()
//            fragmentManager?.beginTransaction()?.apply {
//                replace(R.id.nav_host_fragment_activity_main, loginFragment, LoginFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}