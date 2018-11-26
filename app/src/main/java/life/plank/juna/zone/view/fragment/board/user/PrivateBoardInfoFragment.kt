package life.plank.juna.zone.view.fragment.board.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_private_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.view.adapter.board.user.BoardMembersViewAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject

class PrivateBoardInfoFragment : BaseFragment() {

    @Inject
    lateinit var restApi: RestApi

    var userPosition: Int = 0
    private lateinit var descriptionText: String
    private lateinit var boardId: String
    private lateinit var displayName: String
    private lateinit var boardName: String
    private var userList = ArrayList<User>()

    lateinit var rootView: View
    private var boardMembersViewAdapter: BoardMembersViewAdapter? = null

    companion object {
        private val TAG = PrivateBoardInfoFragment::class.java.simpleName
        fun newInstance(description: String, boardId: String, displayName: String, boardName: String) = PrivateBoardInfoFragment().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.description), description)
                putString(findString(R.string.intent_board_id), boardId)
                putString(findString(R.string.pref_display_name), displayName)
                putString(findString(R.string.board_name), boardName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            descriptionText = getString(getString(R.string.description))!!
            boardId = getString(getString(R.string.intent_board_id))!!
            displayName = getString(getString(R.string.pref_display_name))!!
            boardName = getString(getString(R.string.board_name))!!
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_private_board_info, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        description.text = descriptionText
        initRecyclerView()
        getMembers()
    }

    private fun initRecyclerView() {
        boardMembersViewAdapter = BoardMembersViewAdapter(userList, boardId, this, displayName, boardName)
        board_members_recycler_view.adapter = boardMembersViewAdapter
    }

    private fun getMembers() {
        restApi.getBoardMembers(boardId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    userList = it.body() as ArrayList<User>
                    if (PreferenceManager.CurrentUser.getDisplayName() == displayName) {
                        val inviteUser = User()
                        inviteUser.displayName = getString(R.string.invite_string)
                        userList.add(inviteUser)
                    }
                    boardMembersViewAdapter?.update(userList)
                }
                else -> errorToast(R.string.failed_to_retrieve_members, it)
            }
        })
    }

    fun deletePrivateBoardMember(userId: String) {
        restApi.deleteUserFromPrivateBoard(boardId, userId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: ", it)
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_NO_CONTENT -> {
                    userList.removeAt(userPosition)
                    boardMembersViewAdapter?.notifyDataSetChanged()
                    customToast(R.string.user_deleted)
                }
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    override fun onDestroy() {
        boardMembersViewAdapter = null
        super.onDestroy()
    }
}