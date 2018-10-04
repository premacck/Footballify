package life.plank.juna.zone.view.fragment.board.user;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.BoardMembersViewAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.customview.CustomPopup.showPrivateBoardOptionPopup;

public class PrivateBoardInfoFragment extends Fragment {

    private static final String TAG = PrivateBoardInfoFragment.class.getSimpleName();
    private static final String DESCRIPTION = "description";
    private static final String BOARD_ID = "board_id";
    private static final String DISPLAY_NAME = "display_name";
    private static final String BOARD_NAME = "board_name";
    static BoardMembersViewAdapter boardMembersViewAdapter;
    static ArrayList<User> userList = new ArrayList<>();
    static SharedPreferences sharedPref = ZoneApplication.getContext().getSharedPreferences(ZoneApplication.getContext().getString(R.string.pref_user_details), MODE_PRIVATE);
    private static View rootView;
    private static String boardId;
    private static RestApi staticRestApi;
    private static int userPosition;
    private static String boardName;
    private static String displayName;
    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @BindView(R.id.description)
    TextView descriptionTextView;
    @BindView(R.id.board_members_recycler_view)
    RecyclerView boardMembersRecyclerView;
    private String description;
    private Context context;

    public PrivateBoardInfoFragment() {
    }

    public static PrivateBoardInfoFragment newInstance(String description, String boardId, String displayName, String boardName) {

        PrivateBoardInfoFragment fragment = new PrivateBoardInfoFragment();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION, description);
        args.putString(BOARD_ID, boardId);
        args.putString(DISPLAY_NAME, displayName);
        args.putString(BOARD_NAME, boardName);
        fragment.setArguments(args);
        return fragment;
    }

    public static void onClickProfileImage(View view, String userId, int position) {

        if (sharedPref.getString(ZoneApplication.getContext().getString(R.string.pref_display_name), "NA").equals(displayName)) {
            showPrivateBoardOptionPopup(view, rootView, userId);
            userPosition = position;
        }
    }

    public static void deletePrivateBoardMember(String userId) {

        staticRestApi.deleteUserFromPrivateBoard(boardId, userId, getToken())
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                userList.remove(userPosition);
                                boardMembersViewAdapter.notifyDataSetChanged();
                                Toast.makeText(ZoneApplication.getContext(), R.string.user_deleted, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            description = args.getString(DESCRIPTION);
            boardId = args.getString(BOARD_ID);
            displayName = args.getString(DISPLAY_NAME);
            boardName = args.getString(BOARD_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_private_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        staticRestApi = restApi;
        context = getActivity().getApplicationContext();
        descriptionTextView.setText(description);
        initRecyclerView();
        getMembers();
        return rootView;
    }

    private void initRecyclerView() {
        boardMembersRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        boardMembersViewAdapter = new BoardMembersViewAdapter(userList, context, boardId, this, displayName, picasso, boardName);
        boardMembersRecyclerView.setAdapter(boardMembersViewAdapter);
    }

    private void getMembers() {
        restApi.getBoardMembers(boardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<User>>>() {
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
                                if (sharedPref.getString(ZoneApplication.getContext().getString(R.string.pref_display_name), getString(R.string.na)).equals(displayName)) {
                                    User inviteUser = new User();
                                    inviteUser.setDisplayName(getString(R.string.invite_string));
                                    users.add(inviteUser);
                                }
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