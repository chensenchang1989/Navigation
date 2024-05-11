package com.scc.navigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scc.navigation.data.SearchAddress

class SearchViewModel :ViewModel() {

    val  items = MutableLiveData<SearchAddress>();



}