package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.view.adapter.GetCoinsAdapter;
import life.plank.juna.zone.view.adapter.LastTransactionsAdapter;
import life.plank.juna.zone.view.adapter.MyBoardsAdapter;

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

    private Picasso picasso;
    private MyBoardsAdapter myBoardsAdapter;
    private LastTransactionsAdapter lastTransactionsAdapter;
    private GetCoinsAdapter getCoinsAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        picasso = ((ZoneApplication) getApplicationContext()).getViewComponent().getPicasso();

        initRecyclerViews();
    }

    private void initRecyclerViews() {
        myBoardsAdapter = new MyBoardsAdapter(picasso);
        lastTransactionsAdapter = new LastTransactionsAdapter();
        getCoinsAdapter = new GetCoinsAdapter();
    }

    @OnClick(R.id.create_board_button) public void launchBoardMaker() {
        CreateBoardActivity.launch(this);
    }
}