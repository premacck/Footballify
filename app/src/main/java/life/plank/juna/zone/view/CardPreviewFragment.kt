package life.plank.juna.zone.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.card_preview.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.facilis.BaseCard

class CardPreviewFragment : BaseCard() {

    private var filePath: String? = null

    companion object {

        fun newInstance(mediaFilePath: String) = CardPreviewFragment().apply {
            arguments = Bundle().apply {
                putString(DataUtil.findString(R.string.intent_file_path), mediaFilePath)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            filePath = getString(getString(R.string.intent_file_path))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.card_preview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!filePath.isNullOrEmpty()) {
            val imageBitmap = BitmapFactory.decodeFile(filePath)
            profile_image_view.setImageBitmap(imageBitmap)
        }
    }

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

}