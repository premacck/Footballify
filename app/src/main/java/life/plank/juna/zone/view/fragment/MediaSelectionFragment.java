package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomLinearLayoutManager;
import life.plank.juna.zone.view.adapter.MediaSelectionAdapter;

public class MediaSelectionFragment extends Fragment {
    @BindView(R.id.photos_text_view)
    TextView photosTextView;
    @BindView(R.id.media_container_recycler_view)
    RecyclerView mediaContainerRecyclerView;

    public MediaSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_selection, container, false);
    }

    public void populateStandingRecyclerView() {
        MediaSelectionAdapter mediaSelectionAdapter = new MediaSelectionAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mediaContainerRecyclerView.setLayoutManager(layoutManager);
        mediaContainerRecyclerView.setAdapter(mediaSelectionAdapter);

    }
}
