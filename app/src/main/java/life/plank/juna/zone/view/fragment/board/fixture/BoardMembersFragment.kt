package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_board_members.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.view.adapter.BoardMembersViewAdapter
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class BoardMembersFragment : Fragment() {

    @field: [Inject Named("footballData")]
    lateinit var restApi: RestApi
    @Inject
    lateinit var picasso: Picasso
    private lateinit var boardMembersViewAdapter: BoardMembersViewAdapter
    private var userList = ArrayList<User>()

    private val TAG = BoardMembersFragment::class.java.simpleName

    companion object {
        private lateinit var matchBoardId: String
        fun newInstance(boardId: String?): BoardMembersFragment {
            val fragment = BoardMembersFragment()
            matchBoardId = boardId!!
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board_members, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        initRecyclerView()
        getMembers()
    }

    private fun initRecyclerView() {
        board_members_recycler_view.layoutManager = GridLayoutManager(context, 5)
        //TODO: Send board and display name when invite workflow is fixed
        boardMembersViewAdapter = BoardMembersViewAdapter(userList, context, matchBoardId, this, "", picasso, "")
        board_members_recycler_view.adapter = boardMembersViewAdapter
    }

    private fun getMembers() {
        restApi.getBoardMembers(matchBoardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<List<User>>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "getBoardMembers : Completed")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message)
                    }

                    override fun onNext(response: Response<List<User>>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                boardMembersViewAdapter.update(response.body())
                            }
                            else -> Toast.makeText(context, R.string.failed_to_retrieve_members, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }
}

