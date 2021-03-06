package com.postnikovegor.mobiledev.ui.profile

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentProfileBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()
}