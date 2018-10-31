package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.util.facilis.FragmentUtilKt;
import life.plank.juna.zone.view.activity.base.BaseActivity;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import life.plank.juna.zone.view.adapter.GetCoinsAdapter;
import life.plank.juna.zone.view.adapter.LastTransactionsAdapter;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.fragment.profile.EditProfilePopup;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.DataUtil.equalsNullString;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.facilis.FragmentUtilKt.removeActivePopupsIfAny;

public class UserProfileActivity extends BaseActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    @BindView(R.id.edit_profile_button)
    Button editProfileButton;
    @BindView(R.id.profile_picture_image_view)
    CircleImageView profilePictureImageView;
    @BindView(R.id.name_text_view)
    TextView nameTextView;
    @BindView(R.id.email_text_view)
    TextView emailTextView;
    @BindView(R.id.dob_text_view)
    TextView dobTextView;
    @BindView(R.id.location_text_view)
    TextView locationTextView;
    @BindView(R.id.my_boards_list)
    RecyclerView myBoardsRecyclerView;
    @BindView(R.id.coin_count)
    TextView coinCount;
    @BindView(R.id.last_transactions_list)
    RecyclerView lastTransactionsList;
    @BindView(R.id.get_coins_list)
    RecyclerView getCoinsList;
    @BindView(R.id.settings_toolbar)
    ZoneToolBar toolbar;
    @BindView(R.id.logout_button)
    Button logoutButton;

    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;
    @Inject
    LastTransactionsAdapter lastTransactionsAdapter;
    @Inject
    GetCoinsAdapter getCoinsAdapter;
    private RestApi restApi;
    private UserBoardsAdapter userBoardsAdapter;
    private String username;

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, UserProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        toolbar.setTitle(getString(R.string.settings));
        initRecyclerView();
        getUserBoards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    @OnClick(R.id.edit_profile_button)
    public void editUserProfile() {
        FragmentUtilKt.pushPopup(
                getSupportFragmentManager(),
                R.id.popup_container,
                EditProfilePopup.Companion.newInstance(),
                EditProfilePopup.Companion.getTAG()
        );
    }

    private void initRecyclerView() {
        myBoardsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        userBoardsAdapter = new UserBoardsAdapter(this, restApi, Glide.with(this));
        myBoardsRecyclerView.setAdapter(userBoardsAdapter);
        getCoinsList.setAdapter(getCoinsAdapter);
        lastTransactionsList.setAdapter(lastTransactionsAdapter);
    }

    public void getUserBoards() {
        restApi.getUserBoards(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Board>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<Board>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                List<Board> boards = response.body();
                                Board board = new Board(getString(R.string.new_));
                                if (boards != null) {
                                    boards.add(board);
                                }
                                userBoardsAdapter.setUserBoards(boards);
                                userBoardsAdapter.notifyDataSetChanged();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(UserProfileActivity.this, R.string.cannot_find_user_boards, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(UserProfileActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void getUserDetails() {
        restApi.getUser(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        User model = response.body();
                        if (model != null) {
                            nameTextView.setText(model.getDisplayName());
                            emailTextView.setText(model.getEmailAddress());
                            username = model.getDisplayName();
                            if (model.getProfilePictureUrl() != null) {
                                picasso.load(model.getProfilePictureUrl()).into(profilePictureImageView);
                                toolbar.setProfilePic(model.getProfilePictureUrl());
                            }
                            String location;
                            if (!isNullOrEmpty(model.getCity()) && !equalsNullString(model.getCity())) {
                                location = model.getCity() + ", " + model.getCountry();
                            } else {
                                location = model.getCountry();
                            }
                            locationTextView.setText(location);
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.pref_profile_pic_url), model.getProfilePictureUrl()).apply();
                            editor.putString(getString(R.string.pref_display_name), model.getDisplayName());
                        }
                    }
                });
    }

    @OnClick(R.id.home_fab)
    public void goHome() {
        HomeActivity.Companion.launch(this, true);
    }

    @Override
    public void onBackPressed() {
        if (removeActivePopupsIfAny(getSupportFragmentManager())) super.onBackPressed();
    }
}