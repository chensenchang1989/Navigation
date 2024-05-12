package com.scc.navigation.utils

import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object MMKVUtils  {

     private val ADDRESS_KEY = "ADDRESS_KEY"

    fun <T> saveData(data: T) {
        val mmkv = MMKV.defaultMMKV();
        Gson().toJson(data).also {
            mmkv.encode(ADDRESS_KEY, it)
        }
    }

    fun <T> getData(clazz: Class<T>): T? {
        val mmkv = MMKV.defaultMMKV();
        val jsonData = mmkv.decodeString(ADDRESS_KEY);
        return if (jsonData != null) {
            Gson().fromJson(jsonData, clazz)
        } else {
            null
        }
    }

    /**
     * 清空数据
     */
    fun clear() {
        MMKV.defaultMMKV().clear()
    }
}