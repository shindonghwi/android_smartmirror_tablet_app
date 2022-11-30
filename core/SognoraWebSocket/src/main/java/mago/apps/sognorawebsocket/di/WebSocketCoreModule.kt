package mago.apps.sognorawebsocket.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AudioCoreProvideModule {

    @Singleton
    @Provides
    fun provideSognoraWebSocket(): SognoraWebSocket {
        return SognoraWebSocketImpl()
    }
}
