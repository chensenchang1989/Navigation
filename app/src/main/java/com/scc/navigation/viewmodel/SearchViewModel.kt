package com.scc.navigation.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.scc.navigation.base.BaseViewModel
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.ui.SearchActivity

class SearchViewModel() : BaseViewModel() {

    val liveData = MutableLiveData<List<SearchAddress>>()

    private lateinit var placesClient: PlacesClient

    fun init(activity: SearchActivity){
        placesClient =Places.createClient(activity)
    }

    /**
     * 根据关键字查找
     */
    fun queryAddressByText(text: String) {
        val items = mutableListOf<SearchAddress>()

        // 使用字段定义要返回的数据类型.
        val placeFields =
            listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ID, Place.Field.LAT_LNG)
        val textRequest = SearchByTextRequest.builder(text, placeFields).build();
        showLoading()
        placesClient.searchByText(textRequest)
            .addOnSuccessListener {
                it.places.forEach { it ->
                    items.add(SearchAddress(it.name, it.address, it.id, it.latLng))
                }
                liveData.value = items;
                hideLoading()
            }
            .addOnFailureListener {
                liveData.value = items;
                hideLoading()
            }
    }

}