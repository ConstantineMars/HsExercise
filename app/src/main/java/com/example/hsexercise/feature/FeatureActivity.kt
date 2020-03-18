package com.example.hsexercise.feature

import android.os.Bundle
import com.example.hsexercise.R
import com.example.hsexercise.common.BaseActivity

class FeatureActivity : BaseActivity<FeatureViewModel>() {
    override val viewModelClass = FeatureViewModel::class.java
    override val layoutResId = R.layout.activity_feature

    override fun provideViewModelFactory() = FeatureViewModel.Factory()

    override fun onViewLoad(savedInstanceState: Bundle?) {
        // todo: write code here
    }
}
