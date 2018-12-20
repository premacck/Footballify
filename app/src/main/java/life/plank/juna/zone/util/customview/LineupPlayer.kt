package life.plank.juna.zone.util.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_player_in_lineup.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Formation

class LineupPlayer(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        private var formation: Formation?,
        @ColorRes labelColor: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val name: String
        get() = lineup_player_name.text.toString()

    @JvmOverloads
    constructor(context: Context, formation: Formation? = null, @ColorRes labelColor: Int = R.color.purple) : this(context, null, formation, labelColor) {
        this.formation = formation
    }

    constructor(context: Context, attrs: AttributeSet?, formation: Formation?, @ColorRes labelColor: Int) : this(context, attrs, 0, formation, labelColor) {
        this.formation = formation
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, formation: Formation?, @ColorRes labelColor: Int) : this(context, attrs, defStyleAttr, 0, formation, labelColor) {
        this.formation = formation
    }

    init {
        init(context, labelColor)
    }

    private fun init(context: Context, @ColorRes labelColor: Int) {
        View.inflate(context, R.layout.item_player_in_lineup, this)
        update(labelColor)
    }

    fun update(@ColorRes labelColor: Int) {
        formation?.run {
            setPlayerNumber(number)
                    //    TODO : replace with boolean values once backend filters it
                    .setPlayerCard(
                            yellowCards,
                            redCards,
                            yellowRed)
                    .setSubstituted(substituteOut)
                    .setSolidColor(labelColor)
                    .setPlayerNumber(number)
                    .setGoal(goals)
                    .setName(nickname)
            Glide.with(this@LineupPlayer)
                    .load(imagePath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile))
                    .into(lineup_player_pic)
        }
    }

    private fun setSolidColor(@ColorRes labelColor: Int): LineupPlayer {
        lineup_player_number.solidColor = labelColor
        return this
    }

    private fun setPlayerNumber(number: Int): LineupPlayer {
        this.lineup_player_number.text = number.toString()
        return this
    }

    private fun setPlayerCard(yellowCard: Int, redCard: Int, yellowRed: Int): LineupPlayer {
        if (yellowCard == 0 && redCard == 0 && yellowRed == 0) {
            lineup_player_card.visibility = View.GONE
            return this
        }
        lineup_player_card.visibility = View.VISIBLE
        when {
            yellowRed == 1 -> lineup_player_card.setImageResource(R.drawable.yellow_red)
            redCard == 1 -> lineup_player_card.setImageResource(R.drawable.red_right)
            yellowCard == 1 -> lineup_player_card.setImageResource(R.drawable.yellow_right)
        }
        return this
    }

    private fun setGoal(goalCount: Int): LineupPlayer {
        this.lineup_player_goal.visibility = if (goalCount > 0) View.VISIBLE else View.GONE
        setGoalCount(goalCount)
        lineup_player_goal_count.visibility = if (goalCount > 1) View.VISIBLE else View.GONE
        return this
    }

    private fun setGoalCount(goalCount: Int) {
        lineup_player_goal_count.text = goalCount.toString()
    }

    private fun setSubstituted(isSubstitutedIn: Int): LineupPlayer {
        lineup_player_substitution.visibility = if (isSubstitutedIn == 1) View.VISIBLE else View.GONE
        return this
    }

    private fun setName(name: String): LineupPlayer {
        this.lineup_player_name.text = name
        return this
    }
}