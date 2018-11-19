package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.OnItemClickListener;
import life.plank.juna.zone.view.adapter.SearchViewAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.Auth.getToken;
import static life.plank.juna.zone.util.RestUtilKt.errorToast;

public class InviteToBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnItemClickListener {

    private static final String TAG = InviteToBoardActivity.class.getSimpleName();
    @Inject
    Retrofit retrofit;
    @BindView(R.id.search_view)
    SearchView search;
    @BindView(R.id.title)
    TextView boardTitle;
    @BindView(R.id.invite_user)
    Button inviteButton;
    @Inject
    Picasso picasso;
    Set<User> userSet = new HashSet<>();
    ArrayList<User> userList = new ArrayList<>();
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    private SearchViewAdapter adapter;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initRecyclerView();
        search.setQueryHint(getString(R.string.search_query_hint));
        boardTitle.setText(getIntent().getStringExtra(getString(R.string.board_title)));
    }

    //TODO: Move the card up when the user clicks on the search view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.search_result_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(InviteToBoardActivity.this, 5));
        adapter = new SearchViewAdapter(userList, this, this, picasso);
        recyclerView.setAdapter(adapter);
        search.setOnQueryTextListener(this);
    }

    private void getSearchedUsers(String displayName) {
        restApi.getSearchedUsers(getToken(), displayName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<User>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<List<User>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                adapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                userList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    @OnClick({R.id.invite_user, R.id.blur_background_image_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blur_background_image_view:
                finish();
                break;
            case R.id.invite_user:
                inviteUserToJoinBoard(getIntent().getStringExtra(getString(R.string.intent_board_id)), userSet);
                break;
        }
    }

    private void inviteUserToJoinBoard(String boardId, Set<User> user) {
        restApi.inviteUserToJoinBoard(user, boardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                finish();
                                break;
                            default:
                                errorToast(R.string.invite_user_failed, response);
                                break;
                        }
                    }
                });
    }

    private void setButtonState(Boolean state, Float alpha) {
        inviteButton.setEnabled(state);
        inviteButton.setAlpha(alpha);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            setButtonState(true, 1f);
            getSearchedUsers(s);
        } else {
            setButtonState(false, .5f);
            userList.clear();
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onItemClicked(String objectId, Boolean isSelected) {
        User user = new User();
        user.setObjectId(objectId);
        if (isSelected) {
            userSet.add(user);
        } else {
            userSet.remove(user);
        }
    }
}
