package com.scc.navigation

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.tencent.mmkv.MMKV

class ExamApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }
    }
}