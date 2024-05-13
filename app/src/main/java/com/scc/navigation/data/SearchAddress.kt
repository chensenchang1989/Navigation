package com.scc.navigation.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.geometry.Point
import java.io.Serializable

data class SearchAddress(
    var name: String? = "",
    var address: String? = "",
    var placeId: String? = "",
    var latLng: LatLng?=null
) : Serializable

data class RouteInfo(var points:List<Point> ?=null);

