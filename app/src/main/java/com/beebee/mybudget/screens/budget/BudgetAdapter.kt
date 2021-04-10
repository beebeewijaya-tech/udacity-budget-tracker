package com.beebee.mybudget.screens.budget

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.BudgetItemBinding
import com.beebee.mybudget.domain.Budget
import timber.log.Timber

class BudgetAdapter : ListAdapter<Budget, BudgetAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(private val binding: BudgetItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(budget: Budget) {
            binding.budgetName.text = budget.name
            binding.budgetExpense.text = "Expense ${budget.currentExpense} / ${budget.amount} $ USD"
            val countProgress = ((budget.currentExpense.toDouble() / budget.amount) * 100)
            binding.expenseOver.progress = countProgress.toInt()

            Timber.i("Count progress $countProgress")

            if (budget.currentExpense >= budget.amount) {
                binding.expenseOver.progressTintList = ColorStateList.valueOf(Color.RED)
            } else {
                binding.expenseOver.progressTintList = ColorStateList.valueOf(Color.YELLOW)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: BudgetItemBinding = DataBindingUtil.inflate(inflater, R.layout.budget_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val budget = getItem(position)
        holder.bind(budget)
    }
}