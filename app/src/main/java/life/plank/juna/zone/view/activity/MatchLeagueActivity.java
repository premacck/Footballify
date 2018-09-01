package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.view.adapter.MatchLeagueAdapter;

public class MatchLeagueActivity extends AppCompatActivity {
    String TAG = MatchLeagueActivity.class.getSimpleName();
    @BindView(R.id.league_recycler_view)
    RecyclerView leagueRecyclerView;
    @BindView(R.id.info_text_view)
    TextView infoTextView;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    MatchLeagueAdapter matchLeagueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_league);
        ButterKnife.bind(this);
        populateRecyclerView();
        setUpBoomMenu();
    }

    public void populateRecyclerView() {
        leagueRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        leagueRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        matchLeagueAdapter = new MatchLeagueAdapter(this);
        leagueRecyclerView.setAdapter(matchLeagueAdapter);
    }

    @OnClick({R.id.info_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info_text_view:
                Intent intent = new Intent(this, LeagueInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);

            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
            arcMenu.addItem(child, titles[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0: {
                            startActivity(new Intent(MatchLeagueActivity.this, BoardActivity.class));
                            break;
                        }
                        case 1: {
                            break;
                        }
                        case 2: {
                            break;
                        }
                        case 3: {
                            break;
                        }
                        case 4: {
                            startActivity(new Intent(MatchLeagueActivity.this, CameraActivity.class));
                            break;
                        }
                        case 5: {
                            startActivity(new Intent(MatchLeagueActivity.this, RecordAudioActivity.class));
                            break;
                        }
                        case 6: {
                            break;
                        }
                    }
                }
            });
        }
    }

}
