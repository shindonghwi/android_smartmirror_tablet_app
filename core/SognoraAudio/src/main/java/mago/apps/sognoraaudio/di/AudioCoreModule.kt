package mago.apps.sognoraaudio.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorder
import mago.apps.sognoraaudio.google_tts.SognoraGoogleTTSImpl
import mago.apps.sognoraaudio.media_player.SognoraMediaPlayerImpl
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorderImpl
import mago.apps.sognoraaudio.google_tts.SognoraGoogleTTS
import mago.apps.sognoraaudio.media_player.SognoraMediaPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioCoreModule{

    @Singleton
    @Provides
    fun provideSognoraAudioRecorder(@ApplicationContext context: Context): SognoraAudioRecorder {
        return SognoraAudioRecorderImpl(context)
    }

    @Provides
    fun provideSognoraMediaPlayer(): SognoraMediaPlayer {
        return SognoraMediaPlayerImpl()
    }

    @Singleton
    @Provides
    fun provideSognoraGoogleTTS(@ApplicationContext context: Context): SognoraGoogleTTS {
        return SognoraGoogleTTSImpl(context)
    }
}