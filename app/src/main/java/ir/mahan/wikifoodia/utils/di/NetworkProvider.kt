package ir.mahan.wikifoodia.utils.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mahan.wikifoodia.data.network.ApiServices
import ir.mahan.wikifoodia.utils.Constants.BASE_URL
import ir.mahan.wikifoodia.utils.Constants.CONNECTION_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTime(): Long = CONNECTION_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient(connectionTime: Long, interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .connectTimeout(connectionTime, TimeUnit.SECONDS)
            .readTimeout(connectionTime, TimeUnit.SECONDS)
            .writeTimeout(connectionTime, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

}