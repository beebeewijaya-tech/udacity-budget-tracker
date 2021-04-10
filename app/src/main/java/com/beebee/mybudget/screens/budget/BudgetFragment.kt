package com.beebee.mybudget.screens.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentBudgetBinding
import com.beebee.mybudget.screens.main.MainFragmentDirections
import timber.log.Timber

class BudgetFragment : Fragment() {
    private lateinit var binding: FragmentBudgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetBinding.inflate(inflater, container, false)

        val viewModelFactory = BudgetViewModel.BudgetViewModelFactory(requireActivity().application, requireActivity())
        val viewModel = ViewModelProvider(this, viewModelFactory).get(BudgetViewModel::class.java)

        val adapter = BudgetAdapter()
        binding.budgetList.adapter = adapter

        viewModel.listBudget.observe(viewLifecycleOwner, Observer { list ->
            Timber.i("Hello ${list.toString()}")
            if (list.isNullOrEmpty()) {
                binding.emptyImage.visibility = View.VISIBLE
                binding.emptyDescription.visibility = View.VISIBLE
                binding.emptyTitle.visibility = View.VISIBLE
                binding.budgetList.visibility = View.GONE
            } else {
                binding.emptyImage.visibility = View.GONE
                binding.emptyDescription.visibility = View.GONE
                binding.emptyTitle.visibility = View.GONE
                binding.budgetList.visibility = View.VISIBLE

                adapter.submitList(list)
            }
        })

        binding.fabsBudget.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSaveBudgetFragment())
        }

        return binding.root
    }
}