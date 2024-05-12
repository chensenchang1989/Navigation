package com.scc.navigation.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class SearchAddress(
    val address: String? = "",
    val name: String? = "",
    val placeId: String? = "",
    val latLng: LatLng?=null
) : Serializable

