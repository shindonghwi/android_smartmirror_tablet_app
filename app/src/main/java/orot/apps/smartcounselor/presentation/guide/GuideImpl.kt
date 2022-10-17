package orot.apps.smartcounselor.presentation.guide

interface GuideImpl {
    suspend fun startGuide()
    suspend fun connectSocket()
}