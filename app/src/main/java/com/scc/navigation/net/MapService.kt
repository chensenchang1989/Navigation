package com.scc.navigation.net

import com.scc.navigation.data.RouteInfo
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MapService {

    /**
     * 获取从当前位置到目标位置的路线
     */
    @Headers("Content-Type: application/json")
    @POST(":computeRoutes")
    fun requestRoute(@Body requestBody: RequestBody): Call<RouteInfo>


    companion object {
        fun createService(): MapService {
            return RetrofitClient.createRetrofit().create(MapService::class.java)
        }
    }
}