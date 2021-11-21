package com.postnikovegor.mobiledev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.postnikovegor.mobiledev.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = setupRecyclerView(binding.usersRecyclerView)
        binding.usersRecyclerView.isVisible = false
        binding.progressBar.isVisible = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.userList = loadUsers()
                adapter.notifyDataSetChanged()
                binding.usersRecyclerView.isVisible = true
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView): UserAdapter {
        val adapter = UserAdapter()
        recyclerView.adapter = adapter
        return adapter
    }

    private suspend fun loadUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(1000)
            provideApi().getUsers().data
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