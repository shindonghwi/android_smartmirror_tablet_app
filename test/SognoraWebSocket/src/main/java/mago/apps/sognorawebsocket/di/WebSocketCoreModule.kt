package mago.apps.sognorawebsocket.di


import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketImpl
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketListener
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioCoreProvideModule {

    @Singleton
    @Provides
    fun provideSognoraWebSocket(listener: SognoraWebSocketListener): SognoraWebSocket {
        return SognoraWebSocketImpl(listener)
    }
}
