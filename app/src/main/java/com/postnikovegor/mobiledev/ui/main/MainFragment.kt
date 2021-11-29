package com.postnikovegor.mobiledev.ui.main

import androidx.fragment.app.viewModels
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentMainBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainFragmentViewModel by viewModels()
}