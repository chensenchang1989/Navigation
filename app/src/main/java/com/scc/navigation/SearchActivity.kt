package com.scc.navigation

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

class SearchActivity :AppCompatActivity() {

    lateinit var searchAdapter: SearchAdapter
    override fun getBaseContext(): Context {
        return super.getBaseContext()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }
}