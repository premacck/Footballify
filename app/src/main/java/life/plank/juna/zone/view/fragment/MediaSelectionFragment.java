package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.view.adapter.MediaSelectionAdapter;

public class MediaSelectionFragment extends Fragment {
    @BindView(R.id.photos_text_view)
    TextView photosTextView;
    @BindView(R.id.stickers_text_view)
    TextView stickersTextView;
    @BindView(R.id.gifs_text_view)
    TextView gifTextView;
    @BindView(R.id.media_container_recycler_view)
    RecyclerView mediaContainerRecyclerView;
    @BindView(R.id.close_image_view)
    ImageView crossImageView;
    ArrayList<ChatMediaViewData> mediaData;
    MediaSelectionAdapter mediaSelectionAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_selection, container, false);
    }
}
