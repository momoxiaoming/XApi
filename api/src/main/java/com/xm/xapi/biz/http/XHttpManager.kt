package com.xm.xapi.biz.http

import android.os.Build
import androidx.collection.arrayMapOf
import androidx.core.util.Consumer
import com.xm.xapi.XApi
import com.xm.xapi.biz.log.XLogManager
import com.xm.xapi.ktx.isNetConnected
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * OkHttpHelper
 *
 * @author mmxm
 * @date 2024/9/10 11:56
 */
object XHttpManager {
    private val clientMap = arrayMapOf<String, OkHttpClient>()

    private val dispatcher by lazy {
        Dispatcher()
    }

    /**
     * 获取OkHttpClien
     *
     * @param redirect 是否需要重定向，默认false
     * @param cache 是否使用缓存
     */
    @Synchronized
    fun getOkHttpClient(redirect: Boolean = false, cache: Boolean = false): OkHttpClient {
        val key = getKey(redirect, cache)
        var client = clientMap[key]
        if (client != null) {
            return client
        }
        client = createOkHttpClient() { builder ->
            if (!redirect) {
                builder.followRedirects(false)
                builder.followSslRedirects(false)
            }
            if (cache) {
                builder.addInterceptor(CacheInterceptor())
            }
        }
        clientMap[key] = client
        return client
    }

    /**
     * 创建OkhttpClient,不保存
     */
    fun createOkHttpClient(consumer: Consumer<OkHttpClient.Builder>? = null): OkHttpClient {
        return OkHttpClient.Builder().apply {
            dispatcher(dispatcher)
            //禁止代理
            if (!XLogManager.getDebug()) {
                proxySelector(NoProxySelector())
            }
            //设置缓存目录
            val cacheDir = File(XApi.getApp().cacheDir, "response")
            cache(Cache(cacheDir, 10 * 1024 * 1024L)) //10M
            addInterceptor(UseAgentInterceptor("OkHttp/${OkHttp.VERSION} android/${Build.VERSION.SDK_INT};"))
            connectTimeout(10, TimeUnit.SECONDS)//连接超时设置
            writeTimeout(5 * 60L, TimeUnit.SECONDS)//写入超时设置，
            readTimeout(5 * 60L, TimeUnit.SECONDS)//读取超时设置
            retryOnConnectionFailure(true)

            //日志打印
            val logging = HttpLoggingInterceptor { message ->
                XLogManager.i("http", message)
            }
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(logging)
            consumer?.accept(this)
        }.build()
    }

    fun <T> getApi(
        baseUrl: String,
        clazz: Class<T>,
        redirect: Boolean = false,
        cache: Boolean = false
    ): T {
        return getRetrofit(baseUrl, getOkHttpClient(redirect, cache)).create(clazz)
    }

    fun <T> getApi(
        baseUrl: String,
        clazz: Class<T>,
        consumer: Consumer<OkHttpClient.Builder>? = null
    ): T {
        return getRetrofit(baseUrl, createOkHttpClient(consumer)).create(clazz)
    }

    private fun getRetrofit(url: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    private fun getKey(redirect: Boolean, cache: Boolean): String {
        return "$redirect:$cache"
    }


    /**
     * Use-agent 处理器
     */
    private class UseAgentInterceptor(val useAgent: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            return if (useAgent.isNotEmpty()) {
                val newRequest =
                    request.newBuilder().header("User-Agent", useAgent).method(request.method, request.body)
                        .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(request)
            }
        }
    }

    private class NoProxySelector : ProxySelector() {
        override fun select(uri: URI?): MutableList<Proxy> {
            return arrayListOf(Proxy.NO_PROXY)
        }

        override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
        }

    }

    /**
     * 网络缓存拦截
     */
    private class CacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val originalResponse = chain.proceed(request)
            return if (XApi.getApp().isNetConnected()) {
                originalResponse
            } else {
                //没网络时候缓存4周.
                val maxStale = TimeUnit.DAYS.toSeconds(28) // tolerate 4-weeks stale
                originalResponse.newBuilder().removeHeader("pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale").build()
            }
        }
    }
}