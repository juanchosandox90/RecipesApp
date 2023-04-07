package com.sandoval.recipesapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val  isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    isConnected = true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    isConnected = true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    isConnected = true
                }
            }
        }

        /* connectivityManager.allNetworks.forEach { network ->
             val networkCapability =
                 connectivityManager.getNetworkCapabilities(network)
             networkCapability?.let {
                 if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                     isConnected = true
                     return@forEach
                 }
             }
         }*/

        isNetworkAvailable.value = isConnected
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}