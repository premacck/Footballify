package life.plank.juna.zone.util.masonry.implementation

import android.os.Parcel
import android.os.Parcelable
import android.view.View

import life.plank.juna.zone.util.UIDisplayUtil.getDp

open class AsymmetricViewImpl {
    var numColumns = DEFAULT_COLUMN_COUNT
        protected set
    var requestedHorizontalSpacing: Int = 0
    var requestedColumnWidth: Int = 0
    var requestedColumnCount: Int = 0
    var isAllowReordering: Boolean = false
    var isDebugging: Boolean = false

    init {
        requestedHorizontalSpacing = getDp(5f).toInt()
    }

    fun determineColumns(availableSpace: Int): Int {
        var numColumns: Int

        numColumns = when {
            requestedColumnWidth > 0 -> (availableSpace + requestedHorizontalSpacing) / (requestedColumnWidth + requestedHorizontalSpacing)
            requestedColumnCount > 0 -> requestedColumnCount
            else -> DEFAULT_COLUMN_COUNT
        }

        if (numColumns <= 0) numColumns = 1

        this.numColumns = numColumns

        return numColumns
    }

    fun onSaveInstanceState(superState: Parcelable): Parcelable {
        val ss = SavedState(superState)
        ss.allowReordering = isAllowReordering
        ss.debugging = isDebugging
        ss.numColumns = numColumns
        ss.requestedColumnCount = requestedColumnCount
        ss.requestedColumnWidth = requestedColumnWidth
        ss.requestedHorizontalSpacing = requestedHorizontalSpacing
        return ss
    }

    fun onRestoreInstanceState(ss: SavedState) {
        isAllowReordering = ss.allowReordering
        isDebugging = ss.debugging
        numColumns = ss.numColumns
        requestedColumnCount = ss.requestedColumnCount
        requestedColumnWidth = ss.requestedColumnWidth
        requestedHorizontalSpacing = ss.requestedHorizontalSpacing
    }

    fun getColumnWidth(availableSpace: Int): Int {
        return (availableSpace - (numColumns - 1) * requestedHorizontalSpacing) / numColumns
    }

    class SavedState : View.BaseSavedState {
        var numColumns: Int = 0
        var requestedColumnWidth: Int = 0
        var requestedColumnCount: Int = 0
        var requestedVerticalSpacing: Int = 0
        var requestedHorizontalSpacing: Int = 0
        var defaultPadding: Int = 0
        var debugging: Boolean = false
        var allowReordering: Boolean = false
        var adapterState: Parcelable? = null
        var loader: ClassLoader? = null

        constructor(superState: Parcelable) : super(superState) {}

        constructor(`in`: Parcel) : super(`in`) {

            numColumns = `in`.readInt()
            requestedColumnWidth = `in`.readInt()
            requestedColumnCount = `in`.readInt()
            requestedVerticalSpacing = `in`.readInt()
            requestedHorizontalSpacing = `in`.readInt()
            defaultPadding = `in`.readInt()
            debugging = `in`.readByte().toInt() == 1
            allowReordering = `in`.readByte().toInt() == 1
            adapterState = `in`.readParcelable(loader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)

            dest.writeInt(numColumns)
            dest.writeInt(requestedColumnWidth)
            dest.writeInt(requestedColumnCount)
            dest.writeInt(requestedVerticalSpacing)
            dest.writeInt(requestedHorizontalSpacing)
            dest.writeInt(defaultPadding)
            dest.writeByte((if (debugging) 1 else 0).toByte())
            dest.writeByte((if (allowReordering) 1 else 0).toByte())
            dest.writeParcelable(adapterState, flags)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_COLUMN_COUNT = 3
    }
}
