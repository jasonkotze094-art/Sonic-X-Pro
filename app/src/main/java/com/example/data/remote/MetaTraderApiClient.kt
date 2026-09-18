package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Factory and builder for creating configured [MetaTrader5Service] Retrofit instances.
 */
object MetaTraderApiClient {

    private const val DEFAULT_BASE_URL = "https://api.itradebot.org/"
    private const val TIMEOUT_SECONDS = 30L

    /**
     * Builds an instance of [MetaTrader5Service].
     *
     * @param baseUrl Base URL of the MT5 Gateway / Bridge (defaults to iTradeBot broker bridge)
     * @param apiKey Optional API key / JWT token for broker gateway authentication
     * @param accountId Optional MT5 trading account login ID header
     * @param isDebug Flag to enable HTTP logging
     */
    fun createService(
        baseUrl: String = DEFAULT_BASE_URL,
        apiKey: String? = null,
        accountId: String? = null,
        isDebug: Boolean = false
    ): MetaTrader5Service {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        // Add Authentication & Account Header Interceptor if supplied
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")

            apiKey?.takeIf { it.isNotBlank() }?.let { key ->
                requestBuilder.header("Authorization", "Bearer $key")
                requestBuilder.header("X-API-Key", key)
            }

            accountId?.takeIf { it.isNotBlank() }?.let { id ->
                requestBuilder.header("X-MT5-Account", id)
            }

            chain.proceed(requestBuilder.build())
        }
        okHttpClientBuilder.addInterceptor(authInterceptor)

        // Add HTTP logging in debug mode
        if (isDebug) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl.ensureTrailingSlash())
            .client(okHttpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(MetaTrader5Service::class.java)
    }

    private fun String.ensureTrailingSlash(): String {
        return if (this.endsWith("/")) this else "$this/"
    }
}
