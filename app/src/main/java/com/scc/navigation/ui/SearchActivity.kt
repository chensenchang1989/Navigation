package com.scc.navigation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.maps.android.SphericalUtil
import com.scc.navigation.R
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ActivitySearchBinding
import com.scc.navigation.utils.Constants
import com.scc.navigation.utils.Constants.KEY_CURRENT_POSITION
import com.scc.navigation.utils.Utils
import com.scc.navigation.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONException

class SearchActivity :
    BaseActivity<ActivitySearchBinding, SearchViewModel>(ActivitySearchBinding::inflate) {

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    lateinit var searchAdapter: SearchAdapter

    override val viewModel: SearchViewModel by viewModels()

    private lateinit var placesClient: PlacesClient

    private val items: MutableList<SearchAddress> = ArrayList()


    override fun initListeners() {
        super.initListeners()
        binding.ivBack.setOnClickListener {
            this@SearchActivity.finish()
        }
    }

    override fun onCreateFinished() {
        super.onCreateFinished()
        binding.etSearch.addTextChangedListener(onTextChanged = { text, start, before, count ->
            //获取输入框字段并检索
            if (text?.isNotEmpty() == true) {
                search(text.toString())
            }
        })
        placesClient = Places.createClient(this)
        initRecyclerView()

        //稍微延迟一下弹出键盘
        runBlocking {
            delay(1000)
            Utils.showSoftBoard(this@SearchActivity, binding.etSearch)
        }
    }

    private fun initRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.rvSearch.adapter = searchAdapter;
        searchAdapter.setOnItemClickListener {
            val data = Intent();
            data.putExtra(Constants.KEY_CLICK_ADDRESS, it);
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun search(key: String) {
        // 使用字段定义要返回的数据类型.
        val placeFields =
            listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ID, Place.Field.LAT_LNG)
        val textRequest = SearchByTextRequest.builder(key, placeFields).build();

        placesClient.searchByText(textRequest)
            .addOnSuccessListener {
                it.places.forEach { it ->
                    items.add(SearchAddress(it.name, it.address, it.id, it.latLng))
                }
                searchAdapter.setAddress(items);
            }
            .addOnFailureListener {
                searchAdapter.setAddress(emptyList())
            }
    }

}