package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.AppConstants;

/**
 * Created by plank-hasan on 2/27/2018.
 */

public class ChatDetailsActivity extends AppCompatActivity {
    @BindView(R.id.chat_details_toolbar)
    Toolbar chatDetailsToolbar;
    @BindView(R.id.chat_details_image_view)
    ImageView chatDetailsImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        ButterKnife.bind(this);
        setUpToolBar();
        String imageUrl = getIntent().getStringExtra(AppConstants.CHAT_DETAILS_IMAGE);
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(chatDetailsImageView);
    }

    private void setUpToolBar() {
        chatDetailsToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        chatDetailsToolbar.setTitle(getString(R.string.john_mclntyre));
        setSupportActionBar(chatDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
