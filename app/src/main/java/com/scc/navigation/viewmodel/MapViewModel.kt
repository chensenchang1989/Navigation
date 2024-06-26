package com.scc.navigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.scc.navigation.base.BaseViewModel
import com.scc.navigation.data.RouteInfo
import com.scc.navigation.net.MapService
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : BaseViewModel() {

    val route = MutableLiveData<RouteInfo>()

    fun queryRouteInfo(start: String, des: String) {

        val  jsonObject =JsonObject()
        jsonObject.addProperty("origin",start)
        jsonObject.addProperty("destination",des)
        jsonObject.addProperty("travelMode", 1)

        val json = Gson().toJson(jsonObject)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

        showLoading()
        viewModelScope.launch {
            showLoading()
            val mapService = MapService.createService()
            mapService.requestRoute(requestBody)
                .enqueue(object : Callback<RouteInfo> {

                    override fun onResponse(call: Call<RouteInfo>, response: Response<RouteInfo>) {
                        hideLoading()
                        route.value = response.body();
                    }

                    override fun onFailure(call: Call<RouteInfo>, t: Throwable) {
                        hideLoading()
                        route.value =RouteInfo()
                    }
                })
        }
    }
}