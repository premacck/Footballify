package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 123456;
    private static final int SELECT_PICTURE = 234;
    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    List<Uri> listOfImageOnView = new ArrayList<>();
    private Uri selectedImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_board );
        ButterKnife.bind( this );
        initRecyclerView();
        setUpBoomMenu();
    }

    //todo: Inject adapter
    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 4, GridLayoutManager.VERTICAL, false );
        boardRecyclerView.setLayoutManager( gridLayoutManager );
        listOfImageOnView.add( selectedImageUri );
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
                            getImageResourceFromGallery();
                            break;
                        }
                        case 4: {
                            startActivity( new Intent( BoardActivity.this, CameraActivity.class ) );
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

    //todo: code write in Utils
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = getPathFromURI( selectedImageUri );
                    Toast.makeText( this, "Image Loaded Successfully", Toast.LENGTH_SHORT ).show();
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query( contentUri, proj, null, null, null );
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            res = cursor.getString( column_index );
        }
        cursor.close();
        return res;
    }

    public void getImageResourceFromGallery() {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select Picture" ), SELECT_PICTURE );
        Toast.makeText( BoardActivity.this, "get Image From Gallery", Toast.LENGTH_SHORT ).show();
    }

}
