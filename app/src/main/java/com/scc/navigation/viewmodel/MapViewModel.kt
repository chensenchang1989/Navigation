package com.scc.navigation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.android.geometry.Point
import com.scc.navigation.base.BaseViewModel
import com.scc.navigation.data.RouteInfo
import com.scc.navigation.net.MapService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : BaseViewModel() {

    val route = MutableLiveData<List<Point>?>()

    fun queryRouteInfo(start: String, des: String) {
        showLoading()
        viewModelScope.launch {
            showLoading()
            val mapService = MapService.createService()
            mapService.requestRoute(start, des)
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