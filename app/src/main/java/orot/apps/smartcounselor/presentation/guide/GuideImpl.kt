package orot.apps.smartcounselor.presentation.guide

import kotlinx.coroutines.Job

interface GuideImpl {
    fun startGuide(): Job
    fun connectSocket()
}