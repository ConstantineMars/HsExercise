package com.example.hsexercise.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeatureViewModel : ViewModel() {

    class Factory :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = FeatureViewModel() as T
    }
}
