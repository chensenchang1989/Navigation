package com.scc.navigation.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ActivitySearchBinding
import com.scc.navigation.utils.Constants
import com.scc.navigation.utils.Utils
import com.scc.navigation.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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
        viewModel.init(this@SearchActivity)

        binding.etSearch.addTextChangedListener(onTextChanged = { text, start, before, count ->
            //获取输入框字段并检索
            if (text?.isNotEmpty() == true) {
                viewModel.queryAddressByText(text.toString())
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

        viewModel.liveData.observe(this@SearchActivity, Observer {
            searchAdapter.setAddress(items);
        })
    }

}