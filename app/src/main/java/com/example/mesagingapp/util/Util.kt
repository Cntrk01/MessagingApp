package com.example.mesagingapp.util

import android.app.Activity
import android.content.Context
import androidx.compose.ui.text.font.Typeface
import androidx.core.content.res.ResourcesCompat
import com.example.mesagingapp.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

fun showSuccesfullToast(title:String,message:String,context: Context){
    MotionToast.createToast(
        context as Activity,
        title,
        message,
        MotionToastStyle.SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
}

fun showInfoToast(title:String,message:String,context: Context){
    MotionToast.createToast(
        context as Activity,
        title,
        message,
        MotionToastStyle.INFO,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
}

fun showWarningToast(message:String,title:String?,context: Context){
    MotionToast.createToast(
        context as Activity,
        title,
        message,
        MotionToastStyle.WARNING,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
}

fun showDeleteToast(message:String,title:String?,context: Context){
    MotionToast.createToast(
        context as Activity,
        title,
        message,
        MotionToastStyle.DELETE,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
}




