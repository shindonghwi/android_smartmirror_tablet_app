package mago.apps.sognorawebsocket.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketImpl
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketListener

@Module
@InstallIn(ActivityComponent::class)
object AudioCoreModule{

    @Provides
    fun provideSognoraWebSocket(listener: SognoraWebSocketListener): SognoraWebSocket {
        return SognoraWebSocketImpl(listener)
    }

}