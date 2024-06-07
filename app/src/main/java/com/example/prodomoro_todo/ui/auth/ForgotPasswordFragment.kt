package com.example.prodomoro_todo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.prodomoro_todo.R
import com.example.prodomoro_todo.databinding.FragmentForgotPasswordBinding
import com.example.prodomoro_todo.util.*
import com.example.prodomoro_tododo.ui.auth.LoginFragment

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    val TAG: String = "ForgotPasswordFragment"
    lateinit var binding: FragmentForgotPasswordBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.forgotPasswordButton.setOnClickListener {
            if (validation()){
                viewModel.forgotPassword(binding.username.text.toString())
            }
        }
    }

    private fun observer(){
        viewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.loading.show()
                }
                is UiState.Failure -> {
                    binding.loading.hide()
                    snackbar("error")
                }
                is UiState.Success -> {
                    binding.loading.hide()
                    snackbar(state.data)
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, LoginFragment())
                        ?.commit();
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true
        if (binding.username.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_email))
        }else{
            if (!binding.username.text.toString().isValidEmail()){
                isValid = false
                snackbar(getString(R.string.invalid_email))
            }
        }
        return isValid
    }
}