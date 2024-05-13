package com.scc.navigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scc.navigation.base.BaseViewModel
import com.scc.navigation.data.SearchAddress

class SearchViewModel : BaseViewModel() {

    val  items = MutableLiveData<SearchAddress>();

}