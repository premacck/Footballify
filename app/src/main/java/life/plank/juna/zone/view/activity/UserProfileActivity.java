package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.view.adapter.GetCoinsAdapter;
import life.plank.juna.zone.view.adapter.LastTransactionsAdapter;
import life.plank.juna.zone.view.adapter.MyBoardsAdapter;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity {

    @BindView(R.id.edit_profile_button) Button editProfileButton;
    @BindView(R.id.profile_picture_image_view) CircleImageView profilePictureImageView;
    @BindView(R.id.name_text_view) TextView nameTextView;
    @BindView(R.id.email_text_view) TextView emailTextView;
    @BindView(R.id.dob_text_view) TextView dobTextView;
    @BindView(R.id.location_text_view) TextView locationTextView;

    @BindView(R.id.my_boards_list) RecyclerView myBoardsList;
    @BindView(R.id.create_board_button) ImageButton createBoardButton;
    @BindView(R.id.coin_count) TextView coinCount;
    @BindView(R.id.last_transactions_list) RecyclerView lastTransactionsList;
    @BindView(R.id.get_coins_list) RecyclerView getCoinsList;

    @BindView(R.id.logout_button) Button logoutButton;

    @Inject @Named("default") Retrofit retrofit;
    @Inject Picasso picasso;
    @Inject MyBoardsAdapter myBoardsAdapter;
    @Inject LastTransactionsAdapter lastTransactionsAdapter;
    @Inject GetCoinsAdapter getCoinsAdapter;

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, UserProfileActivity.class));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
    }

    @OnClick(R.id.create_board_button) public void launchBoardMaker() {
        CreateBoardActivity.launch(this);
    }
}