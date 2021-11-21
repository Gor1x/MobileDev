package com.postnikovegor.mobiledev

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        val LOG_TAG = MainActivity::javaClass.name
    }

    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActivityMainBinding::bind)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(LOG_TAG, "onCreate()")

        val adapter = setupRecyclerView(binding.usersRecyclerView)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    Log.d(LOG_TAG, "$viewState")
                    renderViewState(viewState)
                }
            }
        }
    }

    private fun renderViewState(viewState: MainViewModel.ViewState) {
        when (viewState) {
            is MainViewModel.ViewState.Data -> {
                (binding.usersRecyclerView.adapter as UserAdapter).apply {
                    userList = viewState.userList
                    notifyDataSetChanged()
                }
                binding.usersRecyclerView.isVisible = true
                binding.progressBar.isVisible = false
            }
            is MainViewModel.ViewState.Loading -> {
                binding.usersRecyclerView.isVisible = false
                binding.progressBar.isVisible = true
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView): UserAdapter {
        val adapter = UserAdapter()
        recyclerView.adapter = adapter
        return adapter
    }
}