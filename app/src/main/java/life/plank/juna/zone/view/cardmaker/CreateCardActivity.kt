package life.plank.juna.zone.view.cardmaker

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_card.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.view.CardPreviewFragment
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject


class CreateCardActivity : BaseCardActivity() {

    @Inject
    lateinit var restApi: RestApi
    private var filePath: String? = null

    companion object {
        private val TAG = CreateCardActivity::class.java.simpleName
        fun launch(from: Activity, mediaFilePath: String) {
            val intent = Intent(from, CreateCardActivity::class.java)
            if (mediaFilePath.isNotEmpty()) {
                intent.putExtra(from.getString(R.string.intent_file_path), mediaFilePath)
            }
            from.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        ZoneApplication.getApplication().uiComponent.inject(this)

        if (intent.hasExtra(getString(R.string.intent_file_path))) {
            filePath = intent.getStringExtra(getString(R.string.intent_file_path))
        }

        if (!filePath.isNullOrEmpty()) {
            val imageBitmap = BitmapFactory.decodeFile(filePath)
            profile_image_view.setImageBitmap(imageBitmap)
        }
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        camera.onClick {
            CustomCameraActivity.launch(this@CreateCardActivity, true, "", false)
            finish()
        }
        proceed_button.onClick {
            if (!filePath.isNullOrEmpty()) {
                pushFragment(CardPreviewFragment.newInstance(filePath!!))
            }
        }
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

}