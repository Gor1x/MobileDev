package com.postnikovegor.mobiledev.ui.emailconfirmation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentEmailConfirmationBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment
import com.postnikovegor.mobiledev.ui.emailconfirmation.EmailConfirmationViewModel.ResendButtonState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        subscribeToResendButtonState()
        viewBinding.repeatCodeButton.setOnClickListener {
            viewModel.notifyResendCodeButtonClicked()
        }
    }

    private fun subscribeToResendButtonState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resendButtonStateFlow.collect { state ->
                    withContext(Main) {
                        renderButtonState(state)
                    }
                }
            }
        }
    }

    private fun renderButtonState(state: ResendButtonState) {
        when (state) {
            is ResendButtonState.Enabled -> {
                viewBinding.repeatCodeTimer.isVisible = false
                viewBinding.repeatCodeButton.isEnabled = true
            }
            is ResendButtonState.TimerTicking -> {
                viewBinding.repeatCodeTimer.isVisible = true
                viewBinding.repeatCodeTimer.text =
                    timeMillisToTimerText(state.timeRemainSeconds)
                viewBinding.repeatCodeButton.isEnabled = false
            }
        }
    }

    private fun timeMillisToTimerText(secondsRemain: Long): String {
        val minutes = with(secondsRemain / 60) {
            "${this / 10}${this % 10}"
        }
        val seconds = with(secondsRemain % 60) {
            "${this / 10}${this % 10}"
        }
        return "${minutes}:${seconds}"
    }
}