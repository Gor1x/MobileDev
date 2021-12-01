package com.postnikovegor.mobiledev.ui.userlist

import androidx.lifecycle.viewModelScope
import com.postnikovegor.mobiledev.data.network.Api
import com.postnikovegor.mobiledev.entity.User
import com.postnikovegor.mobiledev.ui.MainActivity
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UserListViewModel : BaseViewModel() {

    companion object {
        val LOG_TAG = MainActivity::javaClass.name
    }

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
            val usersResponse = sendLoadUsersRequest()
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

    private suspend fun sendLoadUsersRequest(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                Thread.sleep(200)
                provideApi().getUsers().data
            }
        }
    }

    private fun provideApi(): Api {
        return Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .build()
            .create(Api::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}