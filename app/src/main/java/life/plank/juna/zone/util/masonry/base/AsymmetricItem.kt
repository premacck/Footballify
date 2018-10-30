package life.plank.juna.zone.util.masonry.base

import android.os.Parcelable

interface AsymmetricItem : Parcelable {

    val columnSpan: Int

    val rowSpan: Int
}
