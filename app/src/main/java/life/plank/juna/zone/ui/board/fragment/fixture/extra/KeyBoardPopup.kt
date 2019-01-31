package life.plank.juna.zone.ui.board.fragment.fixture.extra

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import com.prembros.facilis.dialog.BaseBlurPopup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_key_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.data.model.user.User
import javax.inject.Inject

class KeyBoardPopup : BaseBlurPopup(), View.OnLongClickListener, View.OnDragListener {
    private val IMAGE_VIEW_TAG = "CLAP VIEW"

    @Inject
    lateinit var restApi: RestApi

    companion object {
        val TAG: String = KeyBoardPopup::class.java.simpleName
        fun newInstance() = KeyBoardPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_key_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_card.setOnClickListener(null)
        addListeners()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun addListeners() {
        clap_image.setOnLongClickListener(this)
        clap_image.tag = IMAGE_VIEW_TAG

        player_background_color_one.setOnDragListener(this)
        player_background_color_two.setOnDragListener(this)
        //TODO: Replace with player id
        player_background_color_one.tag = "PlayerOne"
        player_background_color_two.tag = "PlayerTwo"
    }

    override fun onLongClick(view: View): Boolean {

        // Create a new ClipData.Item from the ImageView object's tag
        val item = ClipData.Item(view.tag as CharSequence)

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

        val data = ClipData(view.tag.toString(), mimeTypes, item)

        // Instantiates the drag shadow builder.
        val shadowBuilder = View.DragShadowBuilder(view)

        // Starts the drag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data//data to be dragged
                    , shadowBuilder //drag shadow
                    , view//local data about the drag and drop operation
                    , 0//no needed flags);
            )
        } else {
            @Suppress("DEPRECATION")
            view.startDrag(data//data to be dragged
                    , shadowBuilder //drag shadow
                    , view//local data about the drag and drop operation
                    , 0//no needed flags);
            )
        }

        //Set view visibility to INVISIBLE as we are going to drag the view
        view.visibility = View.INVISIBLE
        return true
    }

    // This is the method that the system calls when it dispatches a drag event to the listener
    override fun onDrag(view: View, event: DragEvent): Boolean {
        // Defines a variable to store the action type for the incoming event
        val action = event.action

        // Handles each of the expected events
        when (action) {
            DragEvent.ACTION_DRAG_STARTED ->
                // Determines if this View can accept the dragged data
                return event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)

            DragEvent.ACTION_DRAG_ENTERED -> {

                // Invalidate the view to force a redraw
                view.invalidate()

                return true
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                // Ignore the event
                return true
            DragEvent.ACTION_DRAG_EXITED -> {
                // Invalidate the view to force a redraw
                view.invalidate()

                return true
            }
            DragEvent.ACTION_DROP -> {
                
                postClap(view.tag.toString())
                // Invalidates the view to force
                view.invalidate()

                val v = event.localState as View
                val owner = v.parent as ViewGroup
                owner.removeView(v)//remove the dragged view
                v.visibility = View.INVISIBLE//finally set Visibility to INVISIBLE

                // Returns true. DragEvent.getResult() will return true.
                return true
            }
            DragEvent.ACTION_DRAG_ENDED -> {

                // Invalidates the view to force a redraw
                view.invalidate()
                val vv = event.localState as View

                // Does a getResult(), and displays what happened.
                if (!event.result)
                    vv.visibility = View.VISIBLE

                // returns true; the value is ignored.
                return true
            }
        }
        return false
    }

    private fun postClap(playerId: String) {
        restApi.postClap(playerId, User(id = playerId))
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("postClap()", "ERROR: ", it)
                }, {
                    //TODO: Handle response
                    Log.d("postClap()", "Response: " + it.code())
                }, this)
    }
}

