package com.scc.navigation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.scc.navigation.R
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ActivityMapBinding
import com.scc.navigation.utils.Constants.KEY_CLICK_ADDRESS
import com.scc.navigation.utils.Constants.REQUEST_DESTINATION_CODE
import com.scc.navigation.utils.Utils
import com.scc.navigation.utils.addMarkerExt
import com.scc.navigation.utils.changeCameraPosition
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
    private var mDestination: SearchAddress? = null;//选择的目的地
    private var mStartLat: LatLng? = null//开始坐标
    private var mEndLat: LatLng? = null//目的地

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
     * 地图初始化
     */
    private fun initMap() {
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun initListeners() {
        super.initListeners()
        binding.flDestination.setOnClickListener {
            val data = Intent(this@MapActivity, SearchActivity::class.java)
            startActivityForResult(data, REQUEST_DESTINATION_CODE)
        }
        //开始导航
        binding.btnNavigation.setOnClickListener {
            startNavigation()
        }

        viewModel.route.observe(this@MapActivity, Observer {
            //绘制全程路线

        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setLocationPermissions()
        mMap?.setOnMapLoadedCallback {
            requestLocationPermissions {
                startLocationUpdates()
            }
        }
        updateFinishRoute()
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
            if (mStartLat == null && location != null) {
                mStartLat = LatLng(location.latitude, location.longitude)
            }
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

    private fun updateFinishRoute() {
        //已走路程
        mStartLat?.let {
            mCurrentLocation?.let { current ->
                drawRoute(current)
            }
        }
    }

    /**
     * 绘制当前轨迹路线
     */
    private fun drawRoute(current: LatLng?) {
        if (current == null) return
        mMap?.apply {
            clear()
            zoomArea(
                listOf(mStartLat.isEmpty(), current.isEmpty())
            )
            addMarkerExt(current.isEmpty())
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取到回传的目的地信息
        if (data != null && requestCode == REQUEST_DESTINATION_CODE) {
            mDestination = data.getSerializableExtra(KEY_CLICK_ADDRESS) as SearchAddress;
            mEndLat = mDestination!!.latLng;
        }
    }

    private fun startNavigation() {
        if (binding.btnNavigation.text.equals("导航")) {
            binding.btnNavigation.text = "停止"
            binding.clInfoLayout.visibility = View.GONE

            //TODO 请求获取路线接口
//            viewModel.queryRouteInfo()

        } else {
            binding.btnNavigation.text = "导航"
            binding.clInfoLayout.visibility = View.VISIBLE

        }
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mStartLat!!, 15.0f))
    }

    /**
     * 图片上传
     */
    private fun uploadImage(){
        //获取截图
        val bitmap = Utils.getFragmentScreenshot(mapFragment!!);
        //上传图片
    }

    /**
     * 绘制整条路线
     */
    private fun drawRoute(points: List<LatLng>) {
        mMap?.addPolyline(
            PolylineOptions()
                .addAll(points)
                .width(10f)
                .color(R.color.purple_500)
                .geodesic(true)
        )
    }
}