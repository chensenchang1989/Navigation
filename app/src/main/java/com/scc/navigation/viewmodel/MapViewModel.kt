package com.scc.navigation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.navigation.RoutingOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.maps.android.geometry.Point
import com.scc.navigation.base.BaseViewModel
import com.scc.navigation.data.RouteInfo
import com.scc.navigation.net.MapService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class MapViewModel : BaseViewModel() {

    val route = MutableLiveData<List<Point>?>()

    fun queryRouteInfo(start: String, des: String) {

        val  jsonObject =JsonObject()
        jsonObject.addProperty("origin",start)
        jsonObject.addProperty("destination",des)
        jsonObject.addProperty("travelMode", RoutingOptions.TravelMode.CYCLING)

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
                        val points = response.body()?.points;
                        route.value = points;
                    }

                    override fun onFailure(call: Call<RouteInfo>, t: Throwable) {
                        hideLoading()
                        route.value = emptyList()
                    }
                })
        }
    }
}