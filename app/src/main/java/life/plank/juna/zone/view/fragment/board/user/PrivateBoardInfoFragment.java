package life.plank.juna.zone.view.fragment.board.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.view.adapter.BoardMembersViewAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class PrivateBoardInfoFragment extends Fragment {

    private static final String TAG = PrivateBoardInfoFragment.class.getSimpleName();
    private static final String DESCRIPTION = "description";
    private static final String BOARD_ID = "board_id";

    @Inject
    @Named("default")
    RestApi restApi;
    @BindView(R.id.description)
    TextView descriptionTextView;
    @BindView(R.id.board_members_recycler_view)
    RecyclerView boardMembersRecyclerView;
    BoardMembersViewAdapter boardMembersViewAdapter;
    ArrayList<User> userList = new ArrayList<>();
    private String description;
    private String boardId;
    private Context context;

    public PrivateBoardInfoFragment() {
    }

    public static PrivateBoardInfoFragment newInstance(String description, String boardId) {
        PrivateBoardInfoFragment fragment = new PrivateBoardInfoFragment();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION, description);
        args.putString(BOARD_ID, boardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            description = args.getString(DESCRIPTION);
            boardId = args.getString(BOARD_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_private_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        context = getActivity().getApplicationContext();
        descriptionTextView.setText(description);
        initRecyclerView();
        getMembers();
        return rootView;
    }

    private void initRecyclerView() {
        boardMembersRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        boardMembersViewAdapter = new BoardMembersViewAdapter(userList, context, boardId);
        boardMembersRecyclerView.setAdapter(boardMembersViewAdapter);
    }

    private void getMembers() {
        restApi.getBoardMembers(boardId, getToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<User>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getBoardMembers : Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<User>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<User> users = response.body();
                                User inviteUser = new User();
                                inviteUser.setDisplayName(getString(R.string.invite_string));
                                users.add(inviteUser);
                                boardMembersViewAdapter.update(users);
                                break;
                            default:
                                Toast.makeText(context, R.string.failed_to_retrieve_members, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}