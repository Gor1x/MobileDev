package com.postnikovegor.mobiledev.ui.userlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentUserListBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : BaseFragment(R.layout.fragment_user_list) {

    private val viewBinding by viewBinding(FragmentUserListBinding::bind)

    private val viewModel: UserListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView(viewBinding.usersRecyclerView)
        subscribeToViewState()

        viewBinding.exceptionLayoutTryAgainButton.setOnClickListener {
            viewModel.tryToLoadUsers()
        }

        viewBinding.userListPullToRefreshLayout.setOnRefreshListener {
            viewModel.tryToLoadUsers()
        }
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::renderViewState)
            }
        }
    }

    private fun renderViewState(viewState: UserListViewModel.ViewState) {
        with(viewBinding) {
            listOf(usersRecyclerView, progressBar, retryLayout)
        }.forEach {
            it.isVisible = false
        }

        viewBinding.userListPullToRefreshLayout.isEnabled = false
        viewBinding.userListPullToRefreshLayout.isRefreshing = false

        when (viewState) {
            is UserListViewModel.ViewState.Data -> {
                (viewBinding.usersRecyclerView.adapter as UserAdapter).apply {
                    userList = viewState.userList
                    notifyDataSetChanged()
                }
                viewBinding.usersRecyclerView.isVisible = true
                viewBinding.userListPullToRefreshLayout.isEnabled = true
            }
            is UserListViewModel.ViewState.Loading -> {
                viewBinding.userListPullToRefreshLayout.isRefreshing = true
            }
            is UserListViewModel.ViewState.EmptyList -> {
                viewBinding.retryLayout.isVisible = true
                viewBinding.tryAgainDescriptionText.text =
                    resources.getString(R.string.user_list_description_empty_list)
            }
            is UserListViewModel.ViewState.Failure -> {
                viewBinding.retryLayout.isVisible = true
                viewBinding.tryAgainDescriptionText.text =
                    resources.getString(R.string.user_list_description_downloading_failed)
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView): UserAdapter =
        UserAdapter().also {
            recyclerView.adapter = it
        }
}