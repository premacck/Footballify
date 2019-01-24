package life.plank.juna.zone.view.user.card

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_card_preview.*
import life.plank.juna.zone.R
import life.plank.juna.zone.service.CommonDataService
import life.plank.juna.zone.view.base.fragment.BaseJunaCard

class CardPreviewFragment : BaseJunaCard() {

    private var filePath: String? = null

    companion object {

        fun newInstance(mediaFilePath: String) = CardPreviewFragment().apply {
            arguments = Bundle().apply {
                putString(CommonDataService.findString(R.string.intent_file_path), mediaFilePath)
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
            inflater.inflate(R.layout.fragment_card_preview, container, false)

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