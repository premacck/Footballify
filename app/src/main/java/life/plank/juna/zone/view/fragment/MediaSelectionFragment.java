package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class MediaSelectionFragment extends Fragment {
    @BindView(R.id.photos_text_view)
    TextView photosTextView;
    @BindView(R.id.stickers_text_view)
    TextView stickersTextView;
    @BindView(R.id.gifs_text_view)
    TextView gifsTextView;
    @BindView(R.id.media_container_recycler_view)
    RecyclerView mediaContainerRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_selection, container, false);
    }
}
