package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.dev_bayan_ibrahim.flashcards.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed interface ConnectivityStatus {
    val iconRes: Int
    val hintRes: Int
    data object Available : ConnectivityStatus {
        override val iconRes: Int = R.drawable.ic_cloud
        override val hintRes: Int = R.string.back_online
    }
    data object Unavailable : ConnectivityStatus {
        override val iconRes: Int = R.drawable.ic_cloud_off
        override val hintRes: Int = R.string.offline
    }
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityStatus(): State<ConnectivityStatus> {
    val context = LocalContext.current

    // Creates a State<ConnectivityStatus> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

/**
 * Network utility to get current state of internet connection
 */
private val Context.currentConnectivityState: ConnectivityStatus
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectivityStatus {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    return if (connected) ConnectivityStatus.Available else ConnectivityStatus.Unavailable
}

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
@ExperimentalCoroutinesApi
private fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun NetworkCallback(callback: (ConnectivityStatus) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectivityStatus.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectivityStatus.Unavailable)
        }
    }
}
