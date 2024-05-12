package com.scc.navigation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel:ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading get() = _loading.asStateFlow()

    fun showLoading() {
        _loading.value = true
    }

    fun hideLoading() {
        _loading.value = false
    }
}