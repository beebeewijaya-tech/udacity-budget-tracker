package com.beebee.mybudget.screens.save_expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.FragmentSaveExpenseBinding
import com.beebee.mybudget.utils.action
import com.beebee.mybudget.utils.hideKeyboard
import com.beebee.mybudget.utils.snack
import timber.log.Timber

class SaveExpenseFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentSaveExpenseBinding
    private var listBudget: MutableList<String> = mutableListOf()
    private var listBudgetId: MutableList<String> = mutableListOf()
    private var selectedBudgetId = ""
    private var selectedBudgetName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveExpenseBinding.inflate(inflater, container, false)

        val viewModelFactory = SaveExpenseViewModel.SaveExpenseViewModelFactory(requireActivity().application, requireActivity())
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SaveExpenseViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.chooseBudgetExpense.onItemSelectedListener = this

        binding.saveExpenseButton.setOnClickListener {
            binding.saveExpenseButton.isEnabled = false
            viewModel.submitExpense(selectedBudgetId, selectedBudgetName)
            hideKeyboard()
        }


        viewModel.budgetList.observe(viewLifecycleOwner, Observer { list ->
            list?.map {
                listBudget.add(it.name)
                listBudgetId.add(it.id as String)
            }.let {
                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listBudget)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.chooseBudgetExpense.adapter = arrayAdapter
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            binding.saveExpenseButton.isEnabled = true
            if (error.isNotBlank()) {
                binding.root.snack(error) {
                    action("OK", R.color.yellow, R.color.aqua) { viewModel.clearMessage() }
                }
            }
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success.isNotBlank()) {
                Timber.i("hello $success")
                binding.saveExpenseButton.isEnabled = true
                binding.root.snack(success) {
                    action("OK", R.color.yellow, R.color.aqua) { viewModel.clearMessage() }
                }
                findNavController().popBackStack()
            }
        })

        return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        selectedBudgetId = listBudgetId[position]
        selectedBudgetName = listBudget[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}