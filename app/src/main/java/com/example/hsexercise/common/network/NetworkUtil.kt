package com.example.hsexercise.common.network

import android.content.Context
import android.net.ConnectivityManager

/**
 * Utility to check connectivity
 */

object NetworkUtil {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}