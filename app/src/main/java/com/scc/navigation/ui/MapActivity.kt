package com.scc.navigation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.scc.navigation.R
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ActivityMapBinding
import com.scc.navigation.utils.Constants.KEY_SEARCH_ADDRESS
import com.scc.navigation.utils.Constants.REQUEST_DESTINATION_CODE
import com.scc.navigation.utils.MMKVUtils
import com.scc.navigation.utils.addMarkerExt
import com.scc.navigation.utils.asColor
import com.scc.navigation.utils.changeCameraPosition
import com.scc.navigation.utils.drawPolyline
import com.scc.navigation.utils.isEmpty
import com.scc.navigation.utils.requestLocationPermissions
import com.scc.navigation.utils.zoomArea
import com.scc.navigation.viewmodel.MapViewModel
import io.nlopez.smartlocation.SmartLocation

class MapActivity : BaseActivity<ActivityMapBinding, MapViewModel>(ActivityMapBinding::inflate),
    OnMapReadyCallback {


    override val viewModel: MapViewModel by viewModels()

    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null //地图
    private var mCurrentLocation: LatLng? = null//当前位置坐标
    private var mDestination:SearchAddress?=null;//选择的目的地

    override fun onCreateFinished() {
        super.onCreateFinished()
        initMap()
        requestLocationPermissions {
            if (it) {
                startLocationUpdates()
            }
        }
    }

    /**
     * 地圖初始化
     */
    private fun initMap() {
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun initListeners() {
        super.initListeners()
        binding.flDestination.setOnClickListener {
            val data: Intent = Intent(this@MapActivity, SearchActivity::class.java)
            startActivityForResult(data, REQUEST_DESTINATION_CODE)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setLocationPermissions()
        mMap?.setOnMapLoadedCallback {
            requestLocationPermissions {
                startLocationUpdates()
            }
        }
    }

    override fun observeEvents() {
        super.observeEvents()
    }

    /**
     * 绘制轨迹
     */
    private fun drawPolyline(shape: String) {
        mMap?.drawPolyline(
            shape = shape, colorId = R.color.red.asColor(this@MapActivity)
        )
    }

    @SuppressLint("MissingPermission")
    private fun setLocationPermissions() {
        if (!isLocationPermissions()) {
            return
        }
        mMap?.isMyLocationEnabled = true
    }

    private fun isLocationPermissions() = ActivityCompat.checkSelfPermission(
        this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this@MapActivity, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * 获取当前位置
     */
    private fun startLocationUpdates() {
        SmartLocation.with(this@MapActivity).location().start { location ->
            updateMapLocation(location)
            mCurrentLocation = LatLng(location.latitude, location.longitude)
        }
    }

    /**
     * 更新当前位置
     */
    private fun updateMapLocation(location: Location) {
        mMap?.changeCameraPosition(LatLng(location.latitude, location.longitude))
    }

    /**
     * 绘制当前路线
     */
    private fun drawRoute(selectedAddress: LatLng?) {
        uploadRoute(selectedAddress)
        mMap?.apply {
            clear()
            zoomArea(
                listOf(mCurrentLocation.isEmpty(), selectedAddress.isEmpty())
            )
            addMarkerExt(selectedAddress.isEmpty())
        }
    }


    private fun uploadRoute(selectedAddress: LatLng?) {
        if (mCurrentLocation == null || selectedAddress == null) return
    }

    override fun onResume() {
        super.onResume()
        mCurrentLocation?.let {
            drawRoute(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取到回传的目的地信息
        if (data != null && requestCode == REQUEST_DESTINATION_CODE) {
            mDestination = data.getSerializableExtra(KEY_SEARCH_ADDRESS) as SearchAddress;
        }
    }
}