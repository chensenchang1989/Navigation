package com.scc.navigation.net

import com.google.gson.GsonBuilder
import com.scc.navigation.utils.Constants
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
        /**
         * Retrofit 实例
         */
        fun createRetrofit() : Retrofit {
              val okHttpClient =  Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build()
                // 返回一个 retrofit 实例
                return Retrofit.Builder()
                        .client(okHttpClient) // 让 retrofit 使用 okhttp
                        .baseUrl(Constants.BASE_URL) // api 地址
                        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))// 使用 gson 解析 json
                        .build()
        }


}