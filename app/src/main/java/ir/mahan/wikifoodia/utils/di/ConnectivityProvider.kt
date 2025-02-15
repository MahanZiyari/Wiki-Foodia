package ir.mahan.wikifoodia.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityProvider {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Provides
    @Singleton
    fun provideNetworkRequest(): NetworkRequest =
        NetworkRequest.Builder().apply {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            // Android M
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            // Android R
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
        }.build()


}