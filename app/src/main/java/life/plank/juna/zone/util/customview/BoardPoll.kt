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
import life.plank.juna.zone.data.model.binder.PollBindingModel
import life.plank.juna.zone.data.model.poll.Poll
import life.plank.juna.zone.data.model.poll.PollAnswerRequest
import life.plank.juna.zone.interfaces.PollContainer
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.AppConstants.PollValue.*
import life.plank.juna.zone.util.DateUtil
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.UIDisplayUtil.getDrawableTopTarget
import life.plank.juna.zone.util.facilis.toggleInteraction
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class BoardPoll @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var glide: RequestManager
    private lateinit var pollBindingModel: PollBindingModel
    private lateinit var pollContainer: PollContainer

    init {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.item_poll, this)
        poll_layout.visibility = View.INVISIBLE
        loading_layout.visibility = View.VISIBLE
    }

    fun prepare(glide: RequestManager, pollBindingModel: PollBindingModel, pollContainer: PollContainer) {
        loading_layout.visibility = View.GONE
        poll_layout.visibility = View.VISIBLE
        this.glide = glide
        this.pollBindingModel = pollBindingModel
        this.pollContainer = pollContainer

//        Loading stadium image
        glide.load(pollBindingModel.background).into(background_image_view)

//        Loading logos
        loadImage(pollBindingModel.homeTeamLogo, poll_first_answer, getDp(28f), getDp(28f))
        loadImage(pollBindingModel.awayTeamLogo, poll_third_answer, getDp(28f), getDp(28f))

//        Loading league logo
        glide.load(pollBindingModel.leagueLogo)
                .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
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
        setPollProgress()

        pollBindingModel.poll.run {
            //            Loading question
            poll_header.text = if (userSelection == 0) question else getAnswer(pollBindingModel)

//            Loading team names
            poll_first_answer.text = choices[0].choice
            poll_second_answer.text = choices[1].choice
            poll_third_answer.text = choices[2].choice

//            Loading votes
            total_votes.text = context.getString(R.string._votes, totalVotes)

//            Setting option buttons' state (whether they should be enabled or disabled
            setOptionsState(pollBindingModel.poll.userSelection)
        }

//        Setting on-click listeners
        poll_first_answer.onClick { onPollSelectionPerformed(HOME) }
        poll_second_answer.onClick { onPollSelectionPerformed(DRAW) }
        poll_third_answer.onClick { onPollSelectionPerformed(AWAY) }
    }

    private fun onPollSelectionPerformed(@PollValue pollValue: Int) {
        pollBindingModel.poll.userSelection = pollValue
        pollContainer.onPollSelected(PollAnswerRequest(pollBindingModel.poll.id, pollValue))
        setOptionsState(pollValue)
    }

    fun pollSelected(poll: Poll) {
        background_image_view.imageTintList = ColorStateList.valueOf(context.getColor(R.color.transparent_black_b3))
        pollBindingModel.poll.userSelection = poll.userSelection
        poll_header.text = getAnswer(pollBindingModel)

        val homePercent = "${poll.choices[0].percentage}%"
        poll_first_answer.text = homePercent
        val drawPercent = "${poll.choices[1].percentage}%"
        poll_second_answer.text = drawPercent
        val awayPercent = "${poll.choices[2].percentage}%"
        poll_third_answer.text = awayPercent

    }

    private fun setOptionsState(selectedOption: Int) {
        poll_first_answer.toggleInteraction(selectedOption == 0)
        poll_second_answer.toggleInteraction(selectedOption == 0)
        poll_third_answer.toggleInteraction(selectedOption == 0)
        if (selectedOption == 0) {
            poll_first_answer.alpha = 1f
            poll_second_answer.alpha = 1f
            poll_third_answer.alpha = 1f
        } else {
            poll_first_answer.background = context.getDrawable(R.drawable.bg_red_translucent_card)
            poll_third_answer.background = context.getDrawable(R.drawable.bg_blue_translucent_card)
            when (selectedOption) {
                HOME -> setAlpha(1f, 0.5f, 0.5f)
                AWAY -> setAlpha(0.5f, 0.5f, 1f)
                else -> setAlpha(0.5f, 1f, 0.5f)
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
                .into(getDrawableTopTarget(button))
    }

    private fun getAnswer(pollBindingModel: PollBindingModel): String {
        setPollProgress()
        val stringBuilder = StringBuilder()
        pollBindingModel.run {
            return when (poll.userSelection) {
                HOME -> stringBuilder.append(poll.choices[0].percentage).appendPercentSaysAndResult(homeTeamName)
                AWAY -> stringBuilder.append(poll.choices[2].percentage).appendPercentSaysAndResult(awayTeamName)
                else -> stringBuilder.append(poll.choices[1].percentage).appendPercentSaysAndResult(DRAW_STRING)
            }.toString()
        }
    }

    private fun StringBuilder.appendPercentSaysAndResult(result: String): StringBuilder =
            this.append("%").append(" says ").append(result)

    private fun setPollProgress() {
        pollBindingModel.run {
            poll_progress_bar.visibility = if (poll.userSelection == 0) View.INVISIBLE else View.VISIBLE
            poll_progress_bar.progress = poll.choices[0].percentage
            poll_progress_bar.secondaryProgress = poll.choices[0].percentage + poll.choices[1].percentage
        }
    }
}