package com.scc.navigation.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import com.scc.navigation.utils.collectLast

abstract class BaseActivity<T : ViewBinding, out VM : BaseViewModel>(
    private val bindingFactory: (LayoutInflater) -> T,
) : AppCompatActivity() {

    protected abstract val viewModel: VM

    lateinit var binding: T

    private val loadingDialog: AlertDialog by lazy { loadingDialog() }
    protected open fun onCreateFinished() {}
    protected open fun initListeners() {}
    protected open fun observeEvents() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory.invoke(layoutInflater)
        setContentView(binding.root)

        collectLast(viewModel.loading, ::handleLoading)
        onCreateFinished()
        initListeners()
        observeEvents()
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.cancel()
            loadingDialog.dismiss()
        }
    }

    /**
     * 自定义加载进度框
     */
    private fun loadingDialog(): AlertDialog {
        val dialog = AlertDialog.Builder(this).setCancelable(false).create()
        return dialog
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}