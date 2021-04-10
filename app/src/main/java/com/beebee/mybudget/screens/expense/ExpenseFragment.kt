package com.beebee.mybudget.screens.expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentExpenseBinding
import com.beebee.mybudget.screens.main.MainFragmentDirections

class ExpenseFragment : Fragment() {
    private lateinit var binding: FragmentExpenseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseBinding.inflate(inflater, container, false)

        val viewModelFactory = ExpenseViewModel.ExpenseViewModelFactory(requireActivity().application, requireActivity())
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)

        val adapter = ExpenseAdapter()
        binding.expenseList.adapter = adapter

        viewModel.listExpense.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNullOrEmpty()) {
                binding.expenseList.visibility = View.GONE
                binding.emptyDescription.visibility = View.VISIBLE
                binding.emptyTitle.visibility = View.VISIBLE
                binding.emptyImage.visibility = View.VISIBLE
            } else {
                binding.expenseList.visibility = View.VISIBLE
                binding.emptyDescription.visibility = View.GONE
                binding.emptyTitle.visibility = View.GONE
                binding.emptyImage.visibility = View.GONE

                adapter.submitList(list)
            }
        })

        binding.fabsExpense.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSaveExpenseFragment())
        }

        return binding.root
    }
}