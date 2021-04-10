package com.beebee.mybudget.screens.save_budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentSaveBudgetBinding
import com.beebee.mybudget.utils.action
import com.beebee.mybudget.utils.hideKeyboard
import com.beebee.mybudget.utils.snack
import timber.log.Timber

class SaveBudgetFragment : Fragment() {
    private lateinit var binding: FragmentSaveBudgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveBudgetBinding.inflate(inflater, container, false)

        val viewModelFactory = SaveBudgetViewModel.SaveBudgetViewModelFactory(requireActivity().application, requireActivity())
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SaveBudgetViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.saveBudgetButton.setOnClickListener {
            binding.saveBudgetButton.isEnabled = false
            viewModel.submitBudget()
            hideKeyboard()
        }

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            binding.saveBudgetButton.isEnabled = true
            if (error.isNotBlank()) {
                binding.root.snack(error) {
                    action("OK", R.color.yellow, R.color.aqua) { viewModel.clearMessage() }
                }
            }
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success.isNotBlank()) {
                Timber.i("hello $success")
                binding.saveBudgetButton.isEnabled = true
                binding.root.snack(success) {
                    action("OK", R.color.yellow, R.color.aqua) { viewModel.clearMessage() }
                }
                findNavController().popBackStack()
            }
        })

        return binding.root
    }
}