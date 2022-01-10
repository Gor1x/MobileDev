package com.postnikovegor.mobiledev.ui.news

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentNewsBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private val viewBinding by viewBinding(FragmentNewsBinding::bind)

    private val viewModel: NewsViewModel by viewModels()
}