package com.scc.navigation.data

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class SearchAddress(
    var name: String? = "",
    var address: String? = "",
    var placeId: String? = "",
    var latLng: LatLng?=null
) : Serializable

data class Polyline(val encodedPolyline:String?="")
data class RouteInfo(val distanceMeters:Int?= 0,val duration:String?="",val polyline:Polyline?=null);

