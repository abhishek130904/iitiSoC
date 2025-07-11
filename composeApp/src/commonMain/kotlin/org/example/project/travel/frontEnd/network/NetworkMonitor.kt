package org.example.project.travel.frontEnd.network

import kotlinx.coroutines.flow.StateFlow

expect object NetworkMonitor {
    val isOnline: StateFlow<Boolean>
    fun start()
    fun stop()
} 