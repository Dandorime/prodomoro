package com.example.prodomoro_tododo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.prodomoro_todo.MainActivity
import com.example.prodomoro_todo.R
import com.example.prodomoro_todo.databinding.FragmentLoginBinding
import com.example.prodomoro_todo.ui.auth.AuthViewModel
import com.example.prodomoro_todo.ui.auth.ForgotPasswordFragment
import com.example.prodomoro_todo.util.UiState
import com.example.prodomoro_todo.util.hide
import com.example.prodomoro_todo.util.isValidEmail
import com.example.prodomoro_todo.util.show
import com.example.prodomoro_todo.util.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val TAG: String = "LoginFragment"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.login.setOnClickListener {
            if (validation()) {
                viewModel.login(
                    email = binding.username.text.toString(),
                    password = binding.password.text.toString()
                )
            }
        }
        binding.forgotPassword.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, ForgotPasswordFragment())
                ?.commit();
        }
    }

    private fun observer(){
        viewModel.login.observe(viewLifecycleOwner) { state ->
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
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.username.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_email))
        }else{
            if (!binding.username.text.toString().isValidEmail()){
                isValid = false
                snackbar(getString(R.string.invalid_login))
            }
        }
        if (binding.password.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_password))
        }else{
            if (binding.password.text.toString().length < 8){
                isValid = false
                snackbar(getString(R.string.invalid_login))
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}