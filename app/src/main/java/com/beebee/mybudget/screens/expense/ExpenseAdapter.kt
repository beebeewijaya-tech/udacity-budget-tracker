package com.beebee.mybudget.screens.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.ExpenseItemBinding
import com.beebee.mybudget.domain.Expense

class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(private val binding: ExpenseItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.expense = expense
            binding.executePendingBindings()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ExpenseItemBinding = DataBindingUtil.inflate(inflater, R.layout.expense_item, parent,false) as ExpenseItemBinding
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseAdapter.ViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }
}