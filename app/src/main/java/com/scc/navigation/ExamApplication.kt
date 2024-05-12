package com.scc.navigation

import android.app.Application
import com.tencent.mmkv.MMKV

class ExamApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}