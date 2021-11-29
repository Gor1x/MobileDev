package com.postnikovegor.mobiledev.ui.emailconfirmation

import androidx.fragment.app.viewModels
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentEmailConfirmationBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()
}