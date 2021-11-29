package com.postnikovegor.mobiledev.ui.onboarding

import android.content.Context
import android.os.CountDownTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.postnikovegor.mobiledev.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val VIEWPAGER_TIMEOUT_MS: Long = 4000 // 4 seconds
private const val VIEW_PAGER_PAGES_COUNT = 3

class OnboardingViewModel : BaseViewModel() {
    sealed class VideoSoundState {
        object On : VideoSoundState()
        object Off : VideoSoundState()
    }

    private val _soundStateFlow = MutableStateFlow<VideoSoundState>(VideoSoundState.Off)

    private var videoSoundState: VideoSoundState
        get() = _soundStateFlow.value
        set(newState) {
            _soundStateFlow.value = newState
        }

    val soundState: Flow<VideoSoundState>
        get() = _soundStateFlow.asStateFlow()

    private var player: ExoPlayer? = null

    private val _viewPagerPositionInnerFlow = MutableStateFlow(0)

    private var viewPagerPosition: Int
        get() = _viewPagerPositionInnerFlow.value
        set(newPosition) {
            _viewPagerPositionInnerFlow.value = newPosition
        }

    val viewPagerPositionFlow: Flow<Int>
        get() = _viewPagerPositionInnerFlow.asStateFlow()

    private var timer = createViewPagerTimer().start()

    fun changeVideoSoundState() {
        videoSoundState = when (videoSoundState) {
            is VideoSoundState.On -> {
                VideoSoundState.Off
            }
            is VideoSoundState.Off -> {
                VideoSoundState.On
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        timer.cancel()
    }

    fun getPlayerInstance(context: Context): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                addMediaItem(MediaItem.fromUri("asset:///onboarding.mp4"))
                repeatMode = Player.REPEAT_MODE_ALL
                prepare()
            }
        }
        return player!!
    }

    fun notifyPageSelected(position: Int) {
        timer.cancel()
        timer = createViewPagerTimer().start()
        viewPagerPosition = position
    }

    private fun createViewPagerTimer() =
        object : CountDownTimer(VIEWPAGER_TIMEOUT_MS, VIEWPAGER_TIMEOUT_MS / 4) {
            override fun onTick(p0: Long) = Unit

            override fun onFinish() {
                viewPagerPosition = (viewPagerPosition + 1) % VIEW_PAGER_PAGES_COUNT
            }
        }
}