package com.example.blogapp.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast


fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}


fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.openAppSetting(){
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:$packageName")
    }.let(::startActivity)
}