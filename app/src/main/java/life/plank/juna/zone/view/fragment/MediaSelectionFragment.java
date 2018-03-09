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
import life.plank.juna.zone.util.AppConstants;
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
                    chatMediaViewModel.getAllMedia(mediaData);
                    mediaSelectionAdapter.notifyDataSetChanged();
                    photosTextViewFocused();
                    stickersTextViewNotFocused();
                    gifTextViewNotFocused();
                });

        RxView.clicks(stickersTextView)
                .subscribe(v -> {
                    addStickersData();
                    stickersTextViewFocused();
                    gifTextViewNotFocused();
                    photosTextViewNotFocused();
                });

        RxView.clicks(gifTextView)
                .subscribe(v -> {
                    addGifData();
                    stickersTextViewNotFocused();
                    photosTextViewNotFocused();
                    gifTextViewFocused();
                });
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
        gifTextView.setTextColor(getResources().getColor(R.color.dark_grey));
    }

    private void stickersTextViewNotFocused() {
        stickersTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
        stickersTextView.setTextColor(getResources().getColor(R.color.white));
    }

    private void photosTextViewFocused() {
        photosTextView.setBackgroundColor(getResources().getColor(R.color.white));
        photosTextView.setTextColor(getResources().getColor(R.color.dark_grey));
    }

    private void addGifData() {
        // TODO: 01-03-2018 Replace with server data and remove hardcoded data
        mediaData.clear();
        ChatMediaViewData chatMediaViewModel = new ChatMediaViewData("https://thumbs.gfycat.com/GrimSimplisticGermanspaniel-max-1mb.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/Cs4yoF8IGx25G/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/RbWR9d1tJ1dok/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();

        chatMediaViewModel = new ChatMediaViewData("http://i49.tinypic.com/kdx26b.jpg", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/PbfN8XKjFBpaE/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/T0lTQ0LX3FvpK/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();

        chatMediaViewModel = new ChatMediaViewData("https://i.imgur.com/sYqnosD.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();

        chatMediaViewModel = new ChatMediaViewData("https://thumbs.gfycat.com/ObedientHappyDore-max-1mb.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();
    }

    private void addStickersData() {
        // TODO: 01-03-2018 Replace with server data and remove hardcoded data
        mediaData.clear();
        ChatMediaViewData chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRviCJBVIDqcNsyGTZB8zRKFUb0opxp3ffNh2ExbRe0jOfARi5_", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx3ZyX-KbqMKeI2xLB_IVAPnbYWKM1aeZ72W6acASzlcShNgq0oQ", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://sdl-stickershop.line.naver.jp/products/0/0/1/1302829/android/stickers/12243284.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://stickershop.line-scdn.net/stickershop/v1/product/1278190/LINEStorePC/main@2x.png;compress=true", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://n9neo.files.wordpress.com/2013/09/real-madrid-goal.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://lh3.googleusercontent.com/edk61NIBjCZ1Fm5VFb2y2b6BMdkwH9QSMOIzbx5TTYqVgwhM4KODM_9zNpMcM9NneSsUZOr_chSAPZDX_5W2Uc1zJqmMXhnxy5Glu-cjaF9Lo11GeBa6u8Z_", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://ih1.redbubble.net/image.420209542.6303/sticker,375x360-bg,ffffff.u9.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_eDWkAczray8NDaq_jLXKF85jrS6v8_pKRfqRgU3GFHZ6iArJ_A", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSL3L59QiP55GKnJcwMElHHKDCVf69KDZqNMkxKH9PQHp-NPqZ5ZQ", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);
        mediaSelectionAdapter.notifyDataSetChanged();
        populateMediaSelectionRecyclerView();
    }

}
