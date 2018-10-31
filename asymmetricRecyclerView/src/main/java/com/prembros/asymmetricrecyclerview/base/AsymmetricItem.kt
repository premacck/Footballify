package com.prembros.asymmetricrecyclerview.base

import android.os.Parcelable

interface AsymmetricItem : Parcelable {

    val columnSpan: Int

    val rowSpan: Int
}
