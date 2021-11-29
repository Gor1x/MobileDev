package com.postnikovegor.mobiledev.ui.signin

import androidx.fragment.app.viewModels
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentSignInBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewBinding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()
}