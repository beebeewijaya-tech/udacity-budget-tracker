package com.beebee.mybudget.screens.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentLoginBinding
import com.beebee.mybudget.screens.home.HomeActivity
import com.beebee.mybudget.utils.Token
import com.beebee.mybudget.utils.action
import com.beebee.mybudget.utils.hideKeyboard
import com.beebee.mybudget.utils.snack
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.dontHaveAccount.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            hideKeyboard()
            binding.loginButton.isEnabled = false
            viewModel.submitLogin()
        }

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            binding.loginButton.isEnabled = true
            if (error.isNotBlank()) {
                binding.root.snack(error) {
                    action("OK", R.color.yellow, R.color.aqua) { viewModel.clearMessage() }
                }
            }
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success.isNotBlank()) {
                binding.loginButton.isEnabled = true
                Token.saveUserState(requireActivity(), success)
                viewModel.clearMessage()
                val intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        })

        return binding.root
    }
}