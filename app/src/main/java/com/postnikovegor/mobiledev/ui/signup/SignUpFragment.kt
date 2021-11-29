package com.postnikovegor.mobiledev.ui.signup

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentSignUpBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {

    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()
}