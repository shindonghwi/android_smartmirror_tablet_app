package mago.apps.audiorecoder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import mago.apps.audiorecoder.SognoraAudioRecorder

@Module
@InstallIn(ActivityComponent::class)
object AudioCoreModule{

    @Provides
    fun provideSognoraAudioRecorder(context: Context) = SognoraAudioRecorder(context)

}