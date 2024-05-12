package com.scc.navigation.ui

import android.annotation.SuppressLint
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
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.maps.android.SphericalUtil
import com.scc.navigation.R
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ActivitySearchBinding
import com.scc.navigation.utils.Constants.KEY_CURRENT_POSITION
import com.scc.navigation.viewmodel.SearchViewModel
import org.json.JSONArray
import org.json.JSONException

class SearchActivity :
    BaseActivity<ActivitySearchBinding, SearchViewModel>(ActivitySearchBinding::inflate) {

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    lateinit var searchAdapter: SearchAdapter

    override val viewModel: SearchViewModel by viewModels()

    lateinit var deviceLocation: LatLng

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
       // deviceLocation = intent?.getParcelableExtra<LatLng>(KEY_CURRENT_POSITION)!!
        initRecyclerView()
    }
    //

    private fun initRecyclerView() {
        with(binding) {
            searchAdapter = SearchAdapter()
            binding.rvSearch.adapter = searchAdapter;
        }
    }

    @SuppressLint("MissingPermission")
    private fun search(key: String) {
        val bias: LocationBias = RectangularBounds.newInstance(
            LatLng(22.458744, 88.208162),  // SW lat, lng
            LatLng(22.730671, 88.524896) // NE lat, lng
        )

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        val newRequest = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bias)
            .setCountries("IN")
            .setTypesFilter(listOf(PlaceTypes.ESTABLISHMENT))
            .setQuery(key)
            .build()

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                predictions.forEach {
                    val name = it.getPrimaryText(null)
                    val addr = it.getSecondaryText(null)
                    val placeId = it.placeId
                    items.add(SearchAddress(addr.toString(), name.toString(), placeId, null))
                }
                searchAdapter.setAddress(items)
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: ${exception.message}")
                }
            }
    }

}