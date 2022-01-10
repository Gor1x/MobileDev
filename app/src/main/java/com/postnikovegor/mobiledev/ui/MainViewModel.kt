package com.postnikovegor.mobiledev.ui

import com.postnikovegor.mobiledev.interactor.AuthInteractor
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    suspend fun isAuthorizedFlow(): Flow<Boolean> = authInteractor.isAuthorizedFlow()
}