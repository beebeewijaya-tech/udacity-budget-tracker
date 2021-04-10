package com.beebee.mybudget.screens.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beebee.mybudget.screens.budget.BudgetFragment
import com.beebee.mybudget.screens.expense.ExpenseFragment


class MainTabAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenseFragment()
            1 -> BudgetFragment()
            else -> ExpenseFragment()
        }
    }
}