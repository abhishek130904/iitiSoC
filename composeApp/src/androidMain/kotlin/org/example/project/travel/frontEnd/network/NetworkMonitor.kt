package org.example.project.travel.frontEnd.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object NetworkMonitor {
    private val _isOnline = MutableStateFlow(true)
    actual val isOnline: StateFlow<Boolean> = _isOnline
    private var registered = false
    private lateinit var context: Context

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            _isOnline.value = checkOnline()
        }
    }

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    actual fun start() {
        if (!registered && ::context.isInitialized) {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(receiver, filter)
            _isOnline.value = checkOnline()
            registered = true
        }
    }

    actual fun stop() {
        if (registered && ::context.isInitialized) {
            context.unregisterReceiver(receiver)
            registered = false
        }
    }

    private fun checkOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
} 