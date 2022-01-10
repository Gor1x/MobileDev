package com.postnikovegor.mobiledev.ui.userlist

import androidx.lifecycle.viewModelScope
import com.postnikovegor.mobiledev.entity.User
import com.postnikovegor.mobiledev.interactor.UserListInteractor
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userListInteractor: UserListInteractor
) : BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val userList: List<User>) : ViewState()
        data class Failure(val exception: Throwable) : ViewState()
        object EmptyList : ViewState()
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState>
        get() = _viewState.asStateFlow()

    init {
        tryToLoadUsers()
    }

    fun tryToLoadUsers() {
        viewModelScope.launch {
            _viewState.emit(ViewState.Loading)
            val usersResponse = userListInteractor.sendLoadUsersRequest()
            val stateToEmit = usersResponse.fold(
                onSuccess = {
                    if (it.isEmpty())
                        ViewState.EmptyList
                    else
                        ViewState.Data(it)
                },
                onFailure = {
                    ViewState.Failure(it)
                }
            )
            _viewState.emit(stateToEmit)
        }
    }

}