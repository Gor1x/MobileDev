package com.postnikovegor.mobiledev.ui.signup

import androidx.lifecycle.viewModelScope
import com.postnikovegor.mobiledev.entity.UserRegistrationData
import com.postnikovegor.mobiledev.interactor.RegistrationInteractor
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
    }

    fun signUp(
        userRegistrationData: UserRegistrationData
    ) {
        viewModelScope.launch {
            try {
                registrationInteractor.signUp(userRegistrationData)
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)

                // TODO: Commented for debug purposes
                //_eventChannel.send(Event.SignUpSuccess)

            } catch (error: Exception) {
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            }
        }
    }
}