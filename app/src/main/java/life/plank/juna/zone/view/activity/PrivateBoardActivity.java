package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.adapter.PrivateBoardAdapter;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardActivity extends AppCompatActivity {
    private static final String TAG = PrivateBoardActivity.class.getSimpleName();

    public static RenderScript renderScript;
    @Inject
    @Named("default")

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;
    PrivateBoardAdapter privateBoardAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout_board_engagement)
    RelativeLayout layoutBoardEngagement;
    @BindView(R.id.layout_info_tiles)
    RelativeLayout layoutInfoTiles;
    @BindView(R.id.board_type_title)
    TextView boardTypeTitle;
    @BindView(R.id.lock)
    ImageView lock;

    private ArrayList<FootballFeed> boardFeed = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Device ID", FirebaseInstanceId.getInstance().getToken());
        setContentView(R.layout.activity_private_board);
        ButterKnife.bind(this);

        initRecyclerView();
        setUpBoomMenu();

        layoutBoardEngagement.setBackgroundColor(getColor(R.color.transparent_white_one));
        layoutInfoTiles.setBackgroundColor(getColor(R.color.transparent_white_two));
        boardTypeTitle.setText("Private Board");
        lock.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        privateBoardAdapter = new PrivateBoardAdapter(this, boardFeed);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(privateBoardAdapter);
        renderScript = RenderScript.create(this);

    }

    public void setUpBoomMenu() {

        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.text_icon, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Comment", "Attachment", "Video"};
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
                            break;
                        }
                        case 5: {
                            break;
                        }
                        case 6: {
                            break;
                        }
                        case 7: {
                            break;
                        }
                        case 8: {
                            break;
                        }
                    }
                }
            });
        }
    }

}
