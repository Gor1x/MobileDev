package com.postnikovegor.mobiledev.ui.bookmarks

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentBookmarksBinding
import com.postnikovegor.mobiledev.ui.base.BaseFragment

class BookmarksFragment : BaseFragment(R.layout.fragment_bookmarks) {

    private val viewBinding by viewBinding(FragmentBookmarksBinding::bind)

    private val viewModel: BookmarksViewModel by viewModels()
}