package life.plank.juna.zone.view.activity.settings

import android.app.Activity
import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class SettingsContainerActivity : BaseCardActivity() {

    companion object {
        fun launch(from: Activity, isClearTop: Boolean = true) {
            if (isClearTop) {
                from.startActivity(from.intentFor<SettingsContainerActivity>().clearTop())
            } else {
                from.startActivity<SettingsContainerActivity>()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_container)

//        TODO: push settings fragment here
//        pushFragment(null)
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container
}
