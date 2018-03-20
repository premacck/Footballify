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

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.interfaces.MediaSelectionFragmentActionInterface;
import life.plank.juna.zone.util.ItemDecorationChatMediaView;
import life.plank.juna.zone.view.adapter.MediaSelectionAdapter;
import life.plank.juna.zone.viewmodel.ChatMediaViewModel;

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
    ChatMediaViewModel chatMediaViewModel;
    int gridCount = 4;
    private GridLayoutManager gridLayoutManager;
    private MediaSelectionFragmentActionInterface mediaSelectionInterface;
    private boolean isMediaSelected = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_selection, container, false);
        ButterKnife.bind(this, view);
        mediaData = new ArrayList<>();
        photosTextView.setBackgroundColor(getResources().getColor(R.color.white));
        photosTextView.setTextColor(getResources().getColor(R.color.dark_grey));
        populateMediaSelectionRecyclerView();
        getMediaData();
        return view;
    }

    private void getMediaData() {
        chatMediaViewModel = new ChatMediaViewModel(getActivity());
        chatMediaViewModel.getAllMedia(mediaData);
    }

    public void populateMediaSelectionRecyclerView() {
        mediaSelectionAdapter = new MediaSelectionAdapter(getActivity(), mediaData);
        gridLayoutManager = new GridLayoutManager(getActivity(), gridCount);
        mediaContainerRecyclerView.setLayoutManager(gridLayoutManager);
        mediaContainerRecyclerView.setAdapter(mediaSelectionAdapter);
        mediaContainerRecyclerView.addItemDecoration(new ItemDecorationChatMediaView());
    }

    @OnClick({R.id.photos_text_view, R.id.stickers_text_view, R.id.gifs_text_view, R.id.close_image_view})
    public void onViewClicked(View view) {
        RxView.clicks(photosTextView)
                .subscribe(v -> {
                    mediaData.clear();
                    isMediaSelected = true;
                    chatMediaViewModel.getAllMedia(mediaData);
                    mediaSelectionAdapter.notifyDataSetChanged();
                    photosTextViewFocused();
                    stickersTextViewNotFocused();
                    gifTextViewNotFocused();
                });

        RxView.clicks(stickersTextView)
                .subscribe(v -> {
                    chatMediaViewModel.addStickersData(mediaData);
                    mediaSelectionAdapter.notifyDataSetChanged();
                    populateMediaSelectionRecyclerView();
                    stickersTextViewFocused();
                    gifTextViewNotFocused();
                    photosTextViewNotFocused();
                    isMediaSelected = false;
                });

        RxView.clicks(gifTextView)
                .subscribe(v -> {
                    chatMediaViewModel.addGifData(mediaData);
                    mediaSelectionAdapter.notifyDataSetChanged();
                    stickersTextViewNotFocused();
                    photosTextViewNotFocused();
                    gifTextViewFocused();
                    isMediaSelected = false;
                });

        RxView.clicks(crossImageView)
                .subscribe(v -> {
                    mediaSelectionInterface.closeMediaFragment();
                });
    }

    public void setMediaSelectionInterface(MediaSelectionFragmentActionInterface mediaSelectionInterface) {
        this.mediaSelectionInterface = mediaSelectionInterface;
    }

    private void gifTextViewFocused() {
        gifTextView.setTextColor(getResources().getColor(R.color.dark_grey));
        gifTextView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void photosTextViewNotFocused() {
        photosTextView.setTextColor(getResources().getColor(R.color.white));
        photosTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void stickersTextViewFocused() {
        stickersTextView.setBackgroundColor(getResources().getColor(R.color.white));
        stickersTextView.setTextColor(getResources().getColor(R.color.dark_grey));
    }

    private void gifTextViewNotFocused() {
        gifTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
        gifTextView.setTextColor(getResources().getColor(R.color.white));
    }

    private void stickersTextViewNotFocused() {
        stickersTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
        stickersTextView.setTextColor(getResources().getColor(R.color.white));
    }

    private void photosTextViewFocused() {
        photosTextView.setBackgroundColor(getResources().getColor(R.color.white));
        photosTextView.setTextColor(getResources().getColor(R.color.dark_grey));
    }

    @Override
    public void onResume() {
        if (isMediaSelected) {
            mediaData.clear();
            chatMediaViewModel.getAllMedia(mediaData);
            mediaSelectionAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
