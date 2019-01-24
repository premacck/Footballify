package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.*
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_image_and_title.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.base.initLayout

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class BoardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val glide: RequestManager = Glide.with(this)

    init {
        initLayout(R.layout.item_image_and_title)
//        TODO: extend LinearLayout and un-comment following lines if using <merge> tag in XML layout
//        orientation = LinearLayout.VERTICAL
//        gravity = Gravity.CENTER
//        setPadding(0, getDp(10f).toInt(), 0, getDp(10f).toInt())
    }

    @ModelProp(options = [ModelProp.Option.GenerateStringOverloads])
    fun name(name: CharSequence?) {
        name?.run { title.text = this } ?: title.makeGone()
    }

    @ModelProp
    fun prepare(board: Board) {
        if (board.displayName == findString(R.string.new_)) {
            image.setImageDrawable(findDrawable(R.drawable.new_board_circle))
            image.borderColor = findColor(R.color.white)
        } else {
            when (board.boardType) {
                findString(R.string.board_type_football_match) -> {
                    home_team_logo.visibility = View.VISIBLE
                    visiting_team_logo.visibility = View.VISIBLE

                    glide.load(R.drawable.circle_background_white).into(image)

                    glide.load(board.boardEvent?.homeTeamLogo)
                            .into(home_team_logo)
                    glide.load(board.boardEvent?.awayTeamLogo)
                            .into(visiting_team_logo)
                    when (DateUtil.getMatchTimeValue(board.boardEvent?.matchStartTime, true)) {
                        AppConstants.MatchTimeVal.MATCH_LIVE -> {
                            badge.run {
                                visibility = View.VISIBLE
                                background = findDrawable(R.drawable.bg_board_badge_red)
                                text = AppConstants.LIVE
                            }
                            image.borderColor = findColor(R.color.badge_red)
                        }
                        AppConstants.MatchTimeVal.MATCH_SCHEDULED_TODAY -> {
                            badge.run {
                                visibility = View.VISIBLE
                                background = findDrawable(R.drawable.bg_board_badge_blue)
                                text = AppConstants.SOON
                            }
                            image.borderColor = findColor(R.color.twilight_blue)
                        }
                        AppConstants.MatchTimeVal.MATCH_PAST, AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY, AppConstants.MatchTimeVal.MATCH_SCHEDULED_LATER,
                        AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START_BOARD_ACTIVE, AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START -> {
                            badge.visibility = View.GONE
                            image.borderColor = findColor(R.color.black_76_opaque)
                        }
                    }
                }
                findString(R.string.private_) -> {
                    badge.visibility = View.GONE
                    home_team_logo.visibility = View.GONE
                    visiting_team_logo.visibility = View.GONE
                    glide.load(board.boardIconUrl)
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(image)
                    image.borderColor = Color.parseColor(board.color)
                }
            }
        }
    }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onClick(listener: () -> Unit) = onDebouncingClick { listener() }
}