package com.scc.navigation.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Utils {

    companion object{
        /**
         * 强制键盘弹出
         */
        fun showSoftBoard(activity: Activity,tvInput:EditText){
            val inputManager =activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            inputManager.showSoftInput(tvInput,InputMethodManager.SHOW_FORCED)
        }
    }

}