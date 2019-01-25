package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_zone_user_feed.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.user.UserPreference
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.initLayout

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

        val userPreferences = CurrentUser.userPreferences ?: return
        if (!isNullOrEmpty(userPreferences[0].zonePreferences)) {
            time_to_next_match.showNextMatchOnly(zonePair.second)
        }
    }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onClick(listener: () -> Unit) = onReducingClick { listener() }
}