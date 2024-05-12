package com.scc.navigation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.scc.navigation.utils.Constants.REQUEST_LOCATION_PERMISSION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectLast(flow: Flow<T>, action: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(action)
        }
    }
}

fun Activity.requestLocationPermissions(onRequestPermissionsResult: (Boolean) -> Unit) {
    val coarsePermissionGranted = ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val finePermissionGranted = ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (coarsePermissionGranted && finePermissionGranted) {
        onRequestPermissionsResult(true)
    } else {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_LOCATION_PERMISSION
        )
    }
}

fun GoogleMap.changeCameraPosition(latLng: LatLng, zoom: Float = 15f) {
    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}

fun GoogleMap.zoomArea(list: List<LatLng>,padding : Int = 225) {
    if (list.isEmpty()) return
    val boundsBuilder = LatLngBounds.Builder()
    for (latLngPoint in list) boundsBuilder.include(latLngPoint)
    val latLngBounds = boundsBuilder.build()
    moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding))
}

fun LatLng?.isEmpty() = this?:LatLng(0.0, 0.0)

fun GoogleMap.drawPolyline(shape: String, @ColorRes colorId: Int, with: Float = 10f) {
    if (shape.isEmpty()) return
    shape.let {
        val polyline = PolylineOptions().addAll(PolyUtil.decode(shape)).width(with).geodesic(true)
            .color(colorId)
        addPolyline(polyline)
    }
}
fun GoogleMap.addMarkerExt(position: LatLng) {
    addMarker(
        MarkerOptions()
            .position(position)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    )
}

fun Int.asColor(context: Context) = ContextCompat.getColor(context, this)
