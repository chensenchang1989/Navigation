package com.scc.navigation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment


class Utils {

    companion object{
        /**
         * 强制键盘弹出
         */
        fun showSoftBoard(activity: Activity,tvInput:EditText){
            val inputManager =activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            inputManager.showSoftInput(tvInput,InputMethodManager.SHOW_FORCED)
        }

        /**
         * 截取视图
         */
        fun getFragmentScreenshot(fragment: Fragment): Bitmap? {
            // 确保Fragment已经与窗口关联
            if (fragment.view == null) {
                return null
            }
            val view =fragment.view
            // 创建一个和Fragment视图一样大小的空Bitmap
            val bitmap = Bitmap.createBitmap(view!!.width,view.height,Bitmap.Config.ARGB_8888)
            // 使用Fragment的视图来绘制Bitmap
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }

}