package com.example.suicidepreventiveapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.Result
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.databinding.FragmentHomeBinding
import com.example.suicidepreventiveapp.databinding.FragmentProfileBinding
import com.example.suicidepreventiveapp.ui.activity.LoginActivity
import com.example.suicidepreventiveapp.ui.custom.LoadingDialogBar
import com.example.suicidepreventiveapp.ui.viewmodel.MainViewModel
import com.example.suicidepreventiveapp.ui.viewmodel.ProfileViewModel
import com.example.suicidepreventiveapp.ui.viewmodelfactory.MainModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val factory : MainModelFactory = MainModelFactory.getInstance()
    private val viewModel: MainViewModel by viewModels { factory }

    private lateinit var userPreferences: UserPreferences
    private lateinit var userData: UserModel

    private lateinit var loadingDialogBar: LoadingDialogBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(view.context)
        userData = userPreferences.getUser()

        binding.apply {
            tvFullname.text = userData.username
            tvEmail.text = userData.email
            tvPhoneNumber.text = userData.phone
        }

        loadingDialogBar = LoadingDialogBar(view.context)

        binding.btnLogout.setOnClickListener { logoutUser() }

        viewModel.toastbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { text ->
                Toast.makeText(
                    view.context,
                    text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.loadingDialog.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { status ->
                if (status) {
                    loadingDialogBar.showDialog("Mohon Tunggu...")
                } else {
                    loadingDialogBar.hideDialog()
                }
            }
        }
    }

    fun logoutUser() {
        viewModel.logout(userData.refreshToken!!).observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Success -> {
                    userPreferences.clearUser()

                    val loginPage = Intent(view?.context, LoginActivity::class.java)
                    startActivity(loginPage)
                }

                is Result.Error -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
