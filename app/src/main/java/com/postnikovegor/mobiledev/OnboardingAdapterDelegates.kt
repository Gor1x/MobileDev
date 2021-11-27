package com.postnikovegor.mobiledev

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.postnikovegor.mobiledev.databinding.ItemOnboardingTextBinding

fun onboardingTextAdapterDelegate() =
    adapterDelegateViewBinding<String, CharSequence, ItemOnboardingTextBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemOnboardingTextBinding.inflate(layoutInflater, parent, false)
        },
        block = {
            bind {
                binding.textView.text = item
                binding.textView.maxLines = 2
            }
        }
    )