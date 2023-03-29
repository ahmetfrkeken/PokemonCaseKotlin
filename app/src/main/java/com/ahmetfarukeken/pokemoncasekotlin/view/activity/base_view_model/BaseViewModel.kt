package com.ahmetfarukeken.pokemoncasekotlin.view.activity.base_view_model

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val isHaveOverlayPermissin = MutableLiveData<Boolean>()

    fun isHaveOverlayPermisson(): LiveData<Boolean> {
        return isHaveOverlayPermissin
    }

    fun getOverlayPermissin(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isHaveOverlayPermissin.value = Settings.canDrawOverlays(context)
        }
    }
}