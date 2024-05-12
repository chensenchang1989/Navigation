package com.scc.navigation.ui

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import com.scc.navigation.base.BaseActivity
import com.scc.navigation.databinding.ActivitySearchBinding
import com.scc.navigation.viewmodel.SearchViewModel

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(ActivitySearchBinding::inflate) {

    lateinit var searchAdapter: SearchAdapter

    override val viewModel: SearchViewModel by viewModels()

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
            if(text?.isNotEmpty() == true){
                search(text.toString())
            }
        })
    }
    //
    private fun search(key:String){

    }

}