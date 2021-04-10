package com.beebee.mybudget.screens.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentSignupBinding
import com.beebee.mybudget.utils.action
import com.beebee.mybudget.utils.hideKeyboard
import com.beebee.mybudget.utils.snack

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signupButton.setOnClickListener {
            binding.signupButton.isEnabled = false
            hideKeyboard()
            viewModel.submiRegister()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            binding.signupButton.isEnabled = true
            if (error.isNotBlank()) {
                binding.root.snack(error) {
                    action("OK", R.color.yellow, R.color.aqua) {
                        viewModel.clearMessage()
                    }
                }
            }
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            binding.signupButton.isEnabled = true
            if (success.isNotEmpty()) {
                viewModel.clearAll()
                binding.root.snack(success) {
                    action("OK", R.color.yellow, R.color.aqua) {
                        viewModel.clearMessage()
                        findNavController().popBackStack()
                    }
                }
            }
        })

        return binding.root
    }
}