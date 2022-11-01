package com.kt.android.net

import com.kt.android.bean.BannerData
import com.kt.android.bean.HomeDataList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    companion object {

        private const val BASE_URL = "https://www.wanandroid.com"
        private var service: ApiService? = null

        fun getApi(): ApiService {
            if (null == service) {
                val httpLoggingInterceptor =
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

                val client = OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(ApiService::class.java)
            }

            return service!!
        }
    }
    /**
     * 广告栏
     * https://www.wanandroid.com/banner/json
     *
     * @return 广告栏数据
     */
    @GET("banner/json")
    suspend fun bannerData(): BaseResult<List<BannerData>>

    /**
     * https://www.wanandroid.com/article/list/0/json
     *
     * @return 首页数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeList(@Path("page") page: Int): BaseResult<HomeDataList>

}

/**
 * 网络返回数据基类
 */
data class BaseResult<T>(val errorCode: String, val errorMsg: String, val data: T)