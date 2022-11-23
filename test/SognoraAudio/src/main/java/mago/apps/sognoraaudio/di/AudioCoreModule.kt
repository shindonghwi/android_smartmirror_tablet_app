package mago.apps.sognoraaudio.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorder
import mago.apps.sognoraaudio.google_tts.SognoraGoogleTTSImpl
import mago.apps.sognoraaudio.media_player.SognoraMediaPlayerImpl
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorderImpl
import mago.apps.sognoraaudio.google_tts.SognoraGoogleTTS
import mago.apps.sognoraaudio.media_player.SognoraMediaPlayer

@Module
@InstallIn(ActivityComponent::class)
object AudioCoreModule{

    @Provides
    fun provideSognoraAudioRecorder(context: Context): SognoraAudioRecorder {
        return SognoraAudioRecorderImpl(context)
    }

    @Provides
    fun provideSognoraMediaPlayer(): SognoraMediaPlayer {
        return SognoraMediaPlayerImpl()
    }

    @Provides
    fun provideSognoraGoogleTTS(context: Context): SognoraGoogleTTS {
        return SognoraGoogleTTSImpl(context)
    }
}