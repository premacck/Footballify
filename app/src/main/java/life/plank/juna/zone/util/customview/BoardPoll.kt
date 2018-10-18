package life.plank.juna.zone.util.customview

import android.content.Context
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_poll.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Poll
import life.plank.juna.zone.data.model.binder.PollBindingModel
import life.plank.juna.zone.util.AppConstants.LIVE
import life.plank.juna.zone.util.AppConstants.PollValue
import life.plank.juna.zone.util.AppConstants.PollValue.*
import life.plank.juna.zone.util.DateUtil
import life.plank.juna.zone.util.UIDisplayUtil
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class BoardPoll @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var glide: RequestManager
    private lateinit var pollBindingModel: PollBindingModel

    init {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.item_poll, this)
    }

    fun prepare(glide: RequestManager, pollBindingModel: PollBindingModel) {
        this.glide = glide
        this.pollBindingModel = pollBindingModel
        glide.load(pollBindingModel.background).into(background_image_view)

//        Loading logos
        loadImage(pollBindingModel.homeTeamLogo, poll_first_answer, UIDisplayUtil.getDp(8f), UIDisplayUtil.getDp(12f))
        loadImage(pollBindingModel.awayTeamLogo, poll_third_answer, UIDisplayUtil.getDp(8f), UIDisplayUtil.getDp(12f))

//        Loading league logo
        glide.load(pollBindingModel.leagueLogo)
                .apply(RequestOptions.overrideOf(UIDisplayUtil.getDp(20f).toInt(), UIDisplayUtil.getDp(20f).toInt()))
                .into(league_logo)

//        Loading time to kick-off
        val timeDiffFromNow = pollBindingModel.matchStartTime.time - Date().time
        if (timeDiffFromNow <= 0) {
            time_to_kick_off.text = LIVE
        } else {
            object : CountDownTimer(timeDiffFromNow, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val kickoffString = "${context.getString(R.string.kick_off_in_)}${DateUtil.getHourMinuteSecondFormatDate(Date(millisUntilFinished))}"
                    time_to_kick_off.text = kickoffString
                }

                override fun onFinish() {
                    time_to_kick_off.text = LIVE
                }
            }.start()
        }
        setPollProgress(pollBindingModel.poll)

//        Loading header
        poll_header.text =
                if (pollBindingModel.poll.canAnswer) {
                    context.getString(R.string.who_will_win_today)
                } else {
                    getAnswer(pollBindingModel)
                }

//        Loading team names
        poll_first_answer.text = pollBindingModel.homeTeamName
        poll_third_answer.text = pollBindingModel.awayTeamName

//        Loading votes
        val voteString: String = pollBindingModel.poll.totalVotes.toString() + context.getString(R.string._votes)
        total_votes.text = voteString

//        Setting on-click listeners
        poll_first_answer.onClick { onPollSelectionPerformed(HOME) }
        poll_second_answer.onClick { onPollSelectionPerformed(DRAW) }
        poll_third_answer.onClick { onPollSelectionPerformed(AWAY) }
    }

    private fun onPollSelectionPerformed(@PollValue pollValue: String) {
        pollBindingModel.poll.userAnswer = pollValue
        pollSelected(pollBindingModel.poll)
    }

    private fun pollSelected(poll: Poll) {
        background_image_view.imageTintList = ColorStateList.valueOf(context.getColor(R.color.transparent_black_b3))
        pollBindingModel.poll.canAnswer = false
        pollBindingModel.poll.userAnswer = poll.userAnswer
        poll_header.text = getAnswer(pollBindingModel)

        poll_first_answer.isEnabled = false
        poll_second_answer.isEnabled = false
        poll_third_answer.isEnabled = false

        val homePercent = "${poll.homeTeamPercent}%"
        poll_first_answer.text = homePercent
        val drawPercent = "${poll.drawPercent}%"
        poll_second_answer.text = drawPercent
        val awayPercent = "${poll.awayTeamPercent}%"
        poll_third_answer.text = awayPercent

        poll_first_answer.background = context.getDrawable(R.drawable.bg_red_translucent_card)
        poll_third_answer.background = context.getDrawable(R.drawable.bg_blue_translucent_card)
        when (poll.userAnswer) {
            HOME -> {
                setAlpha(1f, 0.5f, 0.5f)
            }
            AWAY -> {
                setAlpha(0.5f, 0.5f, 1f)
            }
            else -> {
                setAlpha(0.5f, 1f, 0.5f)
            }
        }
    }

    private fun setAlpha(firstAnswerAlpha: Float, secondAnswerAlpha: Float, thirdAnswerAlpha: Float) {
        poll_first_answer.alpha = firstAnswerAlpha
        poll_second_answer.alpha = secondAnswerAlpha
        poll_third_answer.alpha = thirdAnswerAlpha
    }

    private fun loadImage(logo: String, button: Button, width: Float, height: Float) {
        glide.load(logo)
                .apply(RequestOptions.overrideOf(width.toInt(), height.toInt()))
                .into(UIDisplayUtil.getDrawableTopTarget(button))
    }

    private fun getAnswer(pollBindingModel: PollBindingModel): String {
        setPollProgress(pollBindingModel.poll)
        val stringBuilder = StringBuilder()
        return when (pollBindingModel.poll.userAnswer) {
            HOME -> stringBuilder.append(pollBindingModel.poll.homeTeamPercent)
                    .appendPercentSays()
                    .append(pollBindingModel.homeTeamName)
            AWAY -> stringBuilder.append(pollBindingModel.poll.awayTeamPercent)
                    .appendPercentSays()
                    .append(pollBindingModel.awayTeamName)
            else -> stringBuilder.append(pollBindingModel.poll.drawPercent)
                    .appendPercentSays()
                    .append(DRAW)
        }.toString()
    }

    private fun StringBuilder.appendPercentSays(): StringBuilder {
        return this.append("%").append(" says ")
    }

    private fun setPollProgress(poll: Poll) {
        progress_bar.visibility = if (poll.canAnswer) View.INVISIBLE else View.VISIBLE
        progress_bar.progress = poll.homeTeamPercent
        progress_bar.secondaryProgress = poll.homeTeamPercent + poll.drawPercent
    }
}