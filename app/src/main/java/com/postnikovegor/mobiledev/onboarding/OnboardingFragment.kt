package com.postnikovegor.mobiledev.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentOnboardingBinding
import com.postnikovegor.mobiledev.onboarding.OnboardingViewModel.VideoSoundState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)

    private val viewModel: OnboardingViewModel by viewModels()

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = viewModel.getPlayerInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.playerView.player = player
        subscribeToSoundState()
        setUpViewPager()

        viewBinding.volumeControlButton.setOnClickListener {
            viewModel.changeVideoSoundState()
        }

        viewBinding.signInButton.setOnClickListener {
            // TODO: Go to @SignInFragment
            Toast.makeText(requireContext(), "Нажата кнопка войти", Toast.LENGTH_SHORT).show()
        }

        viewBinding.signUpButton.setOnClickListener {
            // TODO: Go to @SignUpFragment
            Toast.makeText(requireContext(), "Нажата кнопка зарегистрироваться", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun subscribeToSoundState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.soundState.collect { state ->
                    when (state) {
                        is VideoSoundState.On -> {
                            viewBinding.volumeControlButton
                                .setImageResource(R.drawable.ic_volume_up_white_24dp)
                            player.volume = 1F
                        }
                        is VideoSoundState.Off -> {
                            viewBinding.volumeControlButton
                                .setImageResource(R.drawable.ic_volume_off_white_24dp)
                            player.volume = 0F

                        }
                    }
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun setUpViewPager() {
        viewBinding.viewPager.apply {
            setTextPages()
            attachDots(viewBinding.onboardingTextTabLayout)

            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            val offsetHorizontal = dpToPx(64)
            val offsetVertical = dpToPx(8)
            setPadding(offsetHorizontal, offsetVertical, offsetHorizontal, offsetVertical)

            setPageTransformer(OnboardingViewPagerTransformer())
        }

        viewBinding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.notifyPageSelected(position)
                }
            }
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewPagerPositionFlow.collect { index ->
                    viewBinding.viewPager.currentItem = index
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    private fun ViewPager2.attachDots(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
    }

    private fun ViewPager2.setTextPages() {
        adapter =
            ListDelegationAdapter(onboardingTextAdapterDelegate()).apply {
                items =
                    listOf(
                        getString(R.string.onboarding_view_pager_text_1),
                        getString(R.string.onboarding_view_pager_text_2),
                        getString(R.string.onboarding_view_pager_text_3)
                    )
            }
    }
}
