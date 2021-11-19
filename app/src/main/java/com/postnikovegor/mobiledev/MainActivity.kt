package com.postnikovegor.mobiledev

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.postnikovegor.mobiledev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = UserAdapter()

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(this.root)
            usersRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL, false
                )
                this.adapter = adapter
            }
        }

        adapter.userList = loadUsers()
        adapter.notifyDataSetChanged()
    }

    private fun loadUsers(): List<User> {
        val list = mutableListOf<User>()
        for (i in 0..10)
            list.add(User("$i", "$i user", "$i group"))
        return list
    }
}