package life.plank.juna.zone.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_user_search.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.adapter.common.SearchViewAdapter
import life.plank.juna.zone.view.fragment.base.SearchablePopup
import java.net.HttpURLConnection
import javax.inject.Inject

class SearchUserPopup : SearchablePopup() {

    @Inject
    lateinit var restApi: RestApi

    private var adapter: SearchViewAdapter? = null
    private var userList: MutableList<User> = ArrayList()
    private lateinit var boardId: String
    private lateinit var boardName: String

    companion object {
        fun newInstance(boardId: String, boardName: String) = SearchUserPopup().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.intent_board_id), boardId)
                putString(findString(R.string.board_title), boardName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run {
            boardId = getString(getString(R.string.intent_board_id))!!
            boardName = getString(getString(R.string.board_title))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_user_search, container, false)

    override fun doOnStart() {
        title.text = getString(R.string.invite_people_to_x_board, boardName)
        initRecyclerView()
        invite_user.onDebouncingClick { inviteUserToJoinBoard() }
    }

    private fun initRecyclerView() {
        adapter = SearchViewAdapter(userList, Glide.with(this))
        search_result_recycler_view.adapter = adapter
    }

    private fun getSearchedUsers(displayName: String) {
        searchSubscription = restApi.getSearchedUsers(getToken(), displayName).setObserverThreadsAndSmartSubscribe({}, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    (it.body() as? ArrayList)?.run {
                        if (!this.isEmpty()) {
                            no_user.visibility = View.GONE
                            search_result_recycler_view.visibility = View.VISIBLE
                            adapter?.update(this)
                        } else {
                            no_user.visibility = View.VISIBLE
                            no_user.setText(R.string.no_users_found)
                            search_result_recycler_view.visibility = View.INVISIBLE
                        }
                    }
                }
                HttpURLConnection.HTTP_NOT_FOUND -> {
                    userList.clear()
                    adapter?.notifyDataSetChanged()
                    no_user.visibility = View.VISIBLE
                    no_user.setText(R.string.no_users_found)
                    search_result_recycler_view.visibility = View.INVISIBLE
                }
                else -> errorToast(R.string.search_for_users_or_handles, it)
            }
        }, this)
    }

    private fun inviteUserToJoinBoard() {
        adapter?.run {
            if (!isNullOrEmpty(selectedUsers)) {
                restApi.inviteUserToJoinBoard(selectedUsers, boardId, getToken()).setObserverThreadsAndSmartSubscribe({}, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_CREATED -> {
                            getParentActivity()?.onBackPressed() ?: dismiss()
                        }
                        else -> errorToast(R.string.invite_user_failed, it)
                    }
                }, this)
            }
        }
    }

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = blur_layout

    override fun searchView(): EditText = search_view

    override fun searchedList(): MutableList<*> = userList

    override fun searchAdapter(): RecyclerView.Adapter<*>? = adapter

    override fun searchAction(searchString: String) = getSearchedUsers(searchString)

    override fun searchDelay(): Long = 500

    override fun setButtonState(state: Boolean, alpha: Float) {
        invite_user.isEnabled = state
        invite_user.alpha = alpha
    }
}