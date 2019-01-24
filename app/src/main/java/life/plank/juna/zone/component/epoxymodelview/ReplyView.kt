package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.onDebouncingClick
import kotlinx.android.synthetic.main.item_base_comment.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.feed.FeedItemComment
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.base.fragment.BaseCommentContainerFragment
import life.plank.juna.zone.view.base.initLayout
import org.jetbrains.anko.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ReplyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var commentContainerFragment: BaseCommentContainerFragment
    private lateinit var parentComment: FeedItemComment
    private var parentCommentPosition: Int = -1
    private var position: Int = -1

    init {
        initLayout(R.layout.item_base_comment)
    }

    @ModelProp
    fun withParentComment(parentComment: FeedItemComment) {
        this.parentComment = parentComment
    }

    @ModelProp
    fun withParentCommentPosition(parentCommentPosition: Int) {
        this.parentCommentPosition = parentCommentPosition
    }

    @ModelProp
    fun withPosition(position: Int) {
        this.position = position
    }

    @ModelProp
    fun withContainerFragment(commentContainerFragment: BaseCommentContainerFragment) {
        this.commentContainerFragment = commentContainerFragment
    }

    @ModelProp
    fun prepare(reply: FeedItemComment) {
        doAsync {
            val formattedReply =
                    SpannableStringBuilder(reply.commenterHandle.semiBold().color(R.color.black))
                            .append(AppConstants.SPACE)
                            .append(reply.message.formatMentions())
            uiThread { comment_text_view.text = formattedReply }
        }

        comment_time_text.text = DateUtil.getCommentDateAndTimeFormat(reply.time)

        Glide.with(this).load(reply.commenterProfilePictureUrl)
                .apply(RequestOptions.overrideOf(getDp(20f).toInt(), getDp(20f).toInt()))
                .into(profile_pic!!)

        reply_text_view.onDebouncingClick {
            commentContainerFragment.replyAction(reply_text_view, this, reply.commenterHandle, parentComment, parentCommentPosition, position)
        }

        like_text_view.onDebouncingClick {}
    }
}