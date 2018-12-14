package life.plank.juna.zone.view.cardmaker

import android.app.Activity
import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class CreateCardActivity : BaseCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        private val TAG = CreateCardActivity::class.java.simpleName
        fun launch(from: Activity) = from.startActivity(from.intentFor<CreateCardActivity>())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        ZoneApplication.getApplication().uiComponent.inject(this)

    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

}