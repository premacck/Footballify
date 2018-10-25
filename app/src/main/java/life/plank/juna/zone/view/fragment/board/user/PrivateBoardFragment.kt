package life.plank.juna.zone.view.fragment.board.user

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.CardView
import android.support.v7.widget.PagerSnapHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_private_board.*
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.faded_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.FeedItem
import life.plank.juna.zone.data.model.Thumbnail
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.OnSwipeTouchListener
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.UIDisplayUtil.loadBitmap
import life.plank.juna.zone.util.setObserverThreadsAndSubscribe
import life.plank.juna.zone.view.activity.UserProfileActivity
import life.plank.juna.zone.view.activity.base.BaseBoardActivity
import life.plank.juna.zone.view.adapter.EmojiAdapter
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.board.fixture.BoardTilesFragment
import retrofit2.Response
import rx.Subscriber
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named

class PrivateBoardFragment : CardTileFragment() {

    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var pagerSnapHelper: PagerSnapHelper

    lateinit var boardId: String
    lateinit var board: Board
    private var pagerAdapter: PrivateBoardPagerAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            setDataReceivedFromPushNotification(intent)
        }
    }

    companion object {
        private val TAG = PrivateBoardFragment::class.java.simpleName
        fun newInstance(board: Board): PrivateBoardFragment = PrivateBoardFragment().apply {
            arguments = Bundle().apply {
                putParcelable(getString(R.string.intent_board), board)
            }
        }

        //        TODO: remove when removing PrivateBoardActivity
        fun deletePrivateBoard() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        if (arguments != null && arguments!!.containsKey(getString(R.string.intent_board))) {
            board = arguments?.getParcelable(getString(R.string.intent_board))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.activity_private_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editor = ZoneApplication.getContext().getSharedPreferences(getString(R.string.pref_user_details), Context.MODE_PRIVATE)
        editor.getString(getString(R.string.pref_display_name), getString(R.string.na))

        boardId = board.id
        if (board.owner.displayName == editor.getString(getString(R.string.pref_display_name), getString(R.string.na))) {
            private_board_toolbar.setUpPrivateBoardPopUp(activity, getString(R.string.private_board_owner_popup))
        } else {
            private_board_toolbar.setUpPrivateBoardPopUp(activity, getString(R.string.private_board_user_popup))
        }

        private_board_toolbar.setTitle(board.name)
        private_board_toolbar.setBoardTitle(if (board.boardType == getString(R.string.public_lowercase)) R.string.public_board else R.string.private_board)
        private_board_toolbar.setLeagueLogo(picasso, board.boardIconUrl)
        private_board_toolbar.setBackgroundColor(Color.parseColor(board.color))
        root_card!!.setCardBackgroundColor(Color.parseColor(board.color))

        setupFullScreenRecyclerViewSwipeGesture(activity!!, recycler_view_drag_area, board_tiles_list_full)

        prepareFullScreenRecyclerView()
        setupViewPagerWithFragments()
        val topic = getString(R.string.board_id_prefix) + board.id
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        board_blur_background_image_view.setOnClickListener { dismissFullScreenRecyclerView() }
    }

    fun setDataReceivedFromPushNotification(intent: Intent) {
        val title = intent.getStringExtra(getString(R.string.intent_comment_title))
        val contentType = intent.getStringExtra(getString(R.string.intent_content_type))
        val thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0)
        val thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0)
        val imageUrl = intent.getStringExtra(getString(R.string.intent_image_url))
        val feed = FeedEntry()
        val feedItem = FeedItem()

        feed.feedItem = feedItem
        feed.feedItem.contentType = contentType
        if (contentType == AppConstants.ROOT_COMMENT) {
            feed.feedItem.title = title
        } else {
            val thumbnail = Thumbnail()
            thumbnail.imageWidth = thumbnailWidth
            thumbnail.imageHeight = thumbnailHeight
            thumbnail.imageUrl = imageUrl
            feed.feedItem.thumbnail = thumbnail
            feed.feedItem.url = imageUrl
        }
        try {
            if (pagerAdapter!!.currentFragment is BoardTilesFragment) {
                (pagerAdapter!!.currentFragment as BoardTilesFragment).updateNewPost(feed)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }

    private fun initBottomSheetRecyclerView() {
        emojiAdapter = EmojiAdapter(ZoneApplication.getContext(), boardId, emojiBottomSheetBehavior)
        emoji_recycler_view.adapter = emojiAdapter
    }

    private fun setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior!!.peekHeight = 0
    }

    override fun prepareFullScreenRecyclerView() {
        setupBottomSheet()
        initBottomSheetRecyclerView()
        pagerSnapHelper.attachToRecyclerView(board_tiles_list_full)
//        TODO: un-comment after making changes to BoardFeedDetailAdapter
//        boardFeedDetailAdapter = BoardFeedDetailAdapter(restApi, boardId, true, emojiBottomSheetBehavior, BOARD)
        board_tiles_list_full.adapter = boardFeedDetailAdapter
    }

    private fun setupViewPagerWithFragments() {
        pagerAdapter = PrivateBoardPagerAdapter(childFragmentManager, board)
        private_board_view_pager.adapter = pagerAdapter
        private_board_toolbar.setupWithViewPager(private_board_view_pager)
    }

    override fun getRootFadedCardLayout(): ViewGroup = faded_card

    override fun getRootCard(): CardView = root_card

    override fun getDragHandle(): View = drag_area

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_board)))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mMessageReceiver)
    }

    override fun updateFullScreenAdapter(feedEntryList: List<FeedEntry>) {
        boardFeedDetailAdapter?.update(feedEntryList)
    }

    override fun moveItem(position: Int, previousPosition: Int) {
        if (pagerAdapter!!.currentFragment is BoardTilesFragment) {
            (pagerAdapter!!.currentFragment as BoardTilesFragment).moveItem(position, previousPosition)
        }
    }

    override fun setBlurBackgroundAndShowFullScreenTiles(setFlag: Boolean, position: Int) {
        isTileFullScreenActive = setFlag
        BaseBoardActivity.boardParentViewBitmap = if (setFlag) loadBitmap(root_layout, root_layout, context) else null
        board_blur_background_image_view.setImageBitmap(BaseBoardActivity.boardParentViewBitmap)

        val listener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                recycler_view_drag_area.visibility = View.INVISIBLE
                board_tiles_list_full.visibility = View.INVISIBLE
                recycler_view_drag_area.translationY = 0f
                board_tiles_list_full.translationY = 0f
                board_blur_background_image_view.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
        val recyclerViewAnimation = AnimationUtils.loadAnimation(context, if (setFlag) R.anim.zoom_in else R.anim.zoom_out)
        val blurBackgroundAnimation = AnimationUtils.loadAnimation(context, if (setFlag) android.R.anim.fade_in else android.R.anim.fade_out)
        if (!setFlag) {
            recyclerViewAnimation.setAnimationListener(listener)
            blurBackgroundAnimation.setAnimationListener(listener)
        }
        board_tiles_list_full.startAnimation(recyclerViewAnimation)
        board_blur_background_image_view.startAnimation(blurBackgroundAnimation)

        if (setFlag) {
            board_tiles_list_full.scrollToPosition(position)
            recycler_view_drag_area.visibility = View.VISIBLE
            board_tiles_list_full.visibility = View.VISIBLE
            board_blur_background_image_view.visibility = View.VISIBLE
        }
    }

    override fun setupFullScreenRecyclerViewSwipeGesture(activity: Activity, recyclerViewDragArea: View, boardTilesFullRecyclerView: View) {
        recyclerViewDragArea.setOnTouchListener(object : OnSwipeTouchListener(activity, recyclerViewDragArea, boardTilesFullRecyclerView) {
            override fun onSwipeDown() {
                dismissFullScreenRecyclerView()
            }
        })
    }

    override fun dismissFullScreenRecyclerView() {
        emojiBottomSheetBehavior!!.peekHeight = 0
        emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        setBlurBackgroundAndShowFullScreenTiles(false, 0)
    }

    fun deletePrivateBoard() {
        restApi.deleteBoard(boardId, getToken()).setObserverThreadsAndSubscribe(object : Subscriber<Response<JsonObject>>() {
            override fun onCompleted() {
                Log.i(TAG, "onCompleted: ")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: $e")
                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show()
            }

            override fun onNext(response: Response<JsonObject>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_NO_CONTENT -> {
                        Toast.makeText(ZoneApplication.getContext(), R.string.board_deletion, Toast.LENGTH_LONG).show()
                        val intent = Intent(ZoneApplication.getContext(), UserProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        ZoneApplication.getContext().startActivity(intent)
                    }
                    else -> Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onBackPressed(): Boolean {
        emojiBottomSheetBehavior!!.peekHeight = 0
        emojiBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        return if (isTileFullScreenActive) {
            setBlurBackgroundAndShowFullScreenTiles(false, 0)
            false
        } else {
            boardFeedDetailAdapter = null
            pagerAdapter = null
            emojiAdapter = null
            true
        }
    }

    internal class PrivateBoardPagerAdapter(fm: FragmentManager, private val board: Board) : FragmentPagerAdapter(fm) {

        var currentFragment: Fragment? = null

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> PrivateBoardInfoFragment.newInstance(board.description, board.id, board.owner.displayName, board.name)
                1 -> BoardTilesFragment.newInstance(board.id, true)
                else -> null
            }
        }

        override fun getCount(): Int = 2

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}