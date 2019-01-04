package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_zone_user_feed.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.view.initLayout

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ZoneView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initLayout(R.layout.item_zone_user_feed)
    }

    @ModelProp
    fun withZone(zonePair: Pair<UserPreference, RestApi>) {
        football.text = zonePair.first.zone?.name

        val userPreferences = PreferenceManager.CurrentUser.getUserPreferences() ?: return
        if (!isNullOrEmpty(userPreferences[0].zonePreferences)) {
            time_to_next_match.showNextMatchOnly(zonePair.second)
        }
    }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onClick(listener: () -> Unit) = onReducingClick { listener() }
}