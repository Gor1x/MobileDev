package com.postnikovegor.mobiledev.ui.emailconfirmation

import android.os.CountDownTimer
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import com.postnikovegor.mobiledev.ui.emailconfirmation.EmailConfirmationViewModel.ResendButtonState.Enabled
import com.postnikovegor.mobiledev.ui.emailconfirmation.EmailConfirmationViewModel.ResendButtonState.TimerTicking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor() : BaseViewModel() {

    sealed class ResendButtonState {
        object Enabled : ResendButtonState()
        class TimerTicking(val timeRemainSeconds: Long) : ResendButtonState()
    }

    private val resendButtonStateFlowInner =
        MutableStateFlow<ResendButtonState>(Enabled)

    val resendButtonStateFlow
        get() = resendButtonStateFlowInner.asStateFlow()

    private var resendButtonStateValue: ResendButtonState
        get() = resendButtonStateFlowInner.value
        set(newValue) {
            resendButtonStateFlowInner.value = newValue
        }

    fun notifyResendCodeButtonClicked() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(msRemain: Long) {
                resendButtonStateValue =
                    TimerTicking(TimeUnit.MILLISECONDS.toSeconds(msRemain))
            }

            override fun onFinish() {
                resendButtonStateValue = Enabled
            }
        }.start()
    }
}