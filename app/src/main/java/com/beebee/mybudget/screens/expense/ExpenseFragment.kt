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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment : Fragment() {
    private lateinit var binding: FragmentExpenseBinding

    @Inject
    lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseBinding.inflate(inflater, container, false)

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