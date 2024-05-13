package com.scc.navigation.net

import com.scc.navigation.data.RouteInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    /**
     * 获取从当前位置到目标位置的路线
     */
    @GET("directions/json")
    fun requestRoute(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Call<RouteInfo>


    companion object {
        fun createService(): MapService {
            return RetrofitClient.createRetrofit().create(MapService::class.java)
        }
    }
}