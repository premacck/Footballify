package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity {

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.following_text_view)
    TextView followingTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Log.e( "Device ID", FirebaseInstanceId.getInstance().getToken() );
        setContentView( R.layout.activity_board );
        ButterKnife.bind( this );
        initRecyclerView();
        setUpBoomMenu();
    }

    //todo: Inject adapter
    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 4, GridLayoutManager.VERTICAL, false );
        boardRecyclerView.setLayoutManager( gridLayoutManager );
        BoardMediaAdapter boardMediaAdapter = new BoardMediaAdapter( this );
        boardRecyclerView.setAdapter( boardMediaAdapter );
        boardRecyclerView.setHasFixedSize( true );
        /*footballFeedAdapter.setPinFeedListener(this);
        footballFeedAdapter.setOnLongPressListener(this);*/
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon( R.drawable.ic_un, R.drawable.ic_close_white );
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate( R.layout.layout_floating_action_button, null );
            //child.setId(i);
            RelativeLayout fabRelativeLayout = child.findViewById( R.id.fab_relative_layout );
            ImageView fabImageVIew = child.findViewById( R.id.fab_image_view );
            fabRelativeLayout.setBackground( ContextCompat.getDrawable( this, backgroundColors[i] ) );
            fabImageVIew.setImageResource( fabImages[i] );
            final int position = i;
            arcMenu.addItem( child, titles[i], new View.OnClickListener() {
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
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Gallery" );
                            intent.putExtra( "API", "BoardActivity" );
                            startActivity( intent );
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Camera" );
                            intent.putExtra( "API", "BoardActivity" );


                            startActivity( intent );
                            break;
                        }
                        case 5: {
                            startActivity( new Intent( BoardActivity.this, RecordAudioActivity.class ) );
                            break;
                        }
                        case 6: {
                            break;
                        }
                    }
                }
            } );
        }
    }

    @OnClick({R.id.following_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.following_text_view:
                if (followingTextView.getText().toString().equalsIgnoreCase( "FOLLOWING" )) {
                    followingTextView.setText( R.string.following );
                    FirebaseMessaging.getInstance().subscribeToTopic( "ManUvsManCity" );
                } else {
                    followingTextView.setText( R.string.unfollow );
                    FirebaseMessaging.getInstance().unsubscribeFromTopic( "ManUvsManCity" );
                }
                break;
        }
    }
}