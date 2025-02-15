@file:Suppress("DEPRECATION")

package ir.mahan.wikifoodia.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NetworkChecker @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest
) : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)
    private var capability: NetworkCapabilities? = null

    fun observeNetworkState(): MutableStateFlow<Boolean> {
        // Register
        connectivityManager.registerNetworkCallback(networkRequest, this)
        // init network
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             return checkForConnection()
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    isNetworkAvailable.value = when (type)  {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return isNetworkAvailable
    }

    private fun checkForConnection(): MutableStateFlow<Boolean> {
        val activeNetwork = connectivityManager.activeNetwork
        //  ActiveNetwork
        if (activeNetwork == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }
        // Capabilities
        capability = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (capability == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }


        isNetworkAvailable.value = when {
            capability!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capability!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }

}