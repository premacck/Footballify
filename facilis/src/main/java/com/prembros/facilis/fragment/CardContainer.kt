package com.prembros.facilis.fragment

import android.view.ViewGroup
import androidx.annotation.ColorRes
import com.prembros.facilis.R
import com.prembros.facilis.activity.BaseCardActivity

interface CardContainer {

    fun getRootView(): ViewGroup?

    fun moveToBackGround()

    fun moveToForeGround()

    fun isFadeOutEnabled(): Boolean = false

    @ColorRes
    fun fadeOutColor(): Int = R.color.fade_out_color

    fun parentActivity(): BaseCardActivity
}