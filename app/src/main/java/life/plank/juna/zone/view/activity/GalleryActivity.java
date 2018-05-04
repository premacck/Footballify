package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.ItemDecorationChatMediaView;
import life.plank.juna.zone.view.adapter.GalleryAdapter;
import life.plank.juna.zone.viewmodel.ChatMediaViewModel;

public class GalleryActivity extends AppCompatActivity {
    @BindView(R.id.preview_view)
    ImageView imageView;
    @BindView(R.id.gallery_recycler_view)
    RecyclerView galleryRecyclerView;
    @BindView(R.id.next)
    ImageView nextImageView;
    GalleryAdapter galleryAdapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<ChatMediaViewData> galleryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        galleryData = new ArrayList<>();
        getDataFromProvider();
        galleryAdapter = new GalleryAdapter(galleryData, this, imageView);
        gridLayoutManager = new GridLayoutManager(this, 4);
        galleryRecyclerView.setLayoutManager(gridLayoutManager);
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.addItemDecoration(new ItemDecorationChatMediaView());
    }

    private void getDataFromProvider() {
        ChatMediaViewModel chatMediaViewModel = new ChatMediaViewModel(this);
        chatMediaViewModel.getAllMedia(galleryData);
    }

    @OnClick({R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(AppConstants.SELECTED_GALLERY_IMAGE, galleryAdapter.getGallerySelected());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }
    }
}
