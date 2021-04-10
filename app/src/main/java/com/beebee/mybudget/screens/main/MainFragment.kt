package com.beebee.mybudget.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beebee.mybudget.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        setTabLayout()
        return binding.root
    }

    private fun setTabLayout() {
        binding.homeViewPager.adapter = MainTabAdapter(this)
        TabLayoutMediator(binding.mainTab, binding.homeViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Expenses"
                1 -> tab.text = "Budget"
            }
        }.attach()
    }
}