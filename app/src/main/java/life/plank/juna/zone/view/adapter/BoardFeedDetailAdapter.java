package life.plank.juna.zone.view.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.ColorHashMap;
import life.plank.juna.zone.view.activity.base.BaseBoardActivity;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.BOARD;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.NEWS;
import static life.plank.juna.zone.util.AppConstants.ROOT_COMMENT;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.pinFeedEntry;
import static life.plank.juna.zone.util.DataUtil.unpinFeedEntry;
import static life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow;
import static life.plank.juna.zone.util.PreferenceManager.PinManager.isFeedItemPinned;
import static life.plank.juna.zone.util.PreferenceManager.PinManager.toggleFeedItemPin;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.displaySnackBar;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class BoardFeedDetailAdapter extends RecyclerView.Adapter<BoardFeedDetailAdapter.FootballFeedDetailViewHolder> {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private String TAG = BoardFeedDetailAdapter.class.getCanonicalName();
    private List<FeedEntry> feedsListItem;
    private RestApi restApi;
    private BaseBoardActivity activity;
    private String boardId;
    private boolean isBoardActive;
    private String target;

    public BoardFeedDetailAdapter(BaseBoardActivity activity, String boardId, boolean isBoardActive, String target) {
        this.boardId = boardId;
        this.isBoardActive = isBoardActive;
        ColorHashMap.HashMaps(activity);
        this.activity = activity;
        this.feedsListItem = new ArrayList<>();
        this.target = target;
    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        getApplication().getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_detail_row, parent, false);
        return new FootballFeedDetailViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        FeedEntry feedEntry = feedsListItem.get(position);
        FeedItem feedItem = feedEntry.getFeedItem();

        feedEntry.getFeedInteractions().setHasPinned(isFeedItemPinned(feedItem));

        if (feedItem.getInteractions() != null) {
            holder.likeCountTextView.setText(String.valueOf(feedItem.getInteractions().getLikes()));
            holder.dislikeCountTextView.setText(String.valueOf(feedItem.getInteractions().getDislikes()));
        }
        if (feedsListItem.get(position).getFeedInteractions().getHasLiked()) {
            int tint = ContextCompat.getColor(activity, R.color.frog_green);
            holder.likeImageView.setImageTintList(ColorStateList.valueOf(tint));
            holder.dislikeImageView.setVisibility(View.INVISIBLE);
            holder.likeCountTextView.setVisibility(View.VISIBLE);
            holder.likeSeparator.setVisibility(View.INVISIBLE);
        } else if (feedsListItem.get(position).getFeedInteractions().getHasDisliked()) {
            int tint = ContextCompat.getColor(activity, R.color.salmon);
            holder.dislikeImageView.setImageTintList(ColorStateList.valueOf(tint));
            holder.likeImageView.setVisibility(View.INVISIBLE);
            holder.dislikeCountTextView.setVisibility(View.VISIBLE);
            holder.likeSeparator.setVisibility(View.INVISIBLE);
        }

        if (feedItem.getUser() != null) {
            Picasso.with(activity)
                    .load(feedItem.getUser().getProfilePictureUrl())
                    .centerInside()
                    .resize((int) getDp(20), (int) getDp(20))
                    .into(holder.profilePic);
        }

        String feedId = feedItem.getId();
        if (feedItem.getUser() != null) {
            holder.userNameTextView.setText(feedItem.getUser().getDisplayName());
        } else {
            SharedPreferences userPref = activity.getSharedPreferences(activity.getString(R.string.pref_login_credentails), 0);
            String userEmailId = userPref.getString(activity.getString(R.string.pref_email_address), "NA");
            holder.userNameTextView.setText(userEmailId);
        }
        holder.feedTitleTextView.setText(feedItem.getTitle());
//        TODO: un-comment after swipe down functionality accounts for the pseudo-views
//        holder.feedTopLayout.setOnTouchListener(new OnSwipeTouchListener(activity, holder.feedTopLayout, holder.itemView) {
//            @Override
//            public void onSwipeDown() {
//                activity.setBlurBackgroundAndShowFullScreenTiles(false, 0);
//            }
//        });

        holder.pinImageView.setImageResource(
                feedEntry.getFeedInteractions().getHasPinned() ?
                        R.drawable.ic_pin_active :
                        R.drawable.ic_pin_inactive
        );

        switch (feedItem.getContentType()) {
            case NEWS:
            case IMAGE: {
                mediaPlayer.stop();
                holder.setVisibilities(View.VISIBLE, View.GONE, View.GONE);
                try {
                    Picasso.with(activity).
                            load(feedItem.getThumbnail().getImageUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(holder.feedImageView);
                } catch (Exception e) {
                    holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case AUDIO: {
                mediaPlayer.stop();
                holder.setVisibilities(View.VISIBLE, View.GONE, View.GONE);
                holder.feedImageView.setImageResource(R.drawable.ic_audio);

                String uri = feedItem.getUrl();
                Uri videoUri = Uri.parse(uri);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(activity, videoUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    mediaPlayer.stop();
                }

                mediaPlayer.start();

                try {
                    Picasso.with(activity).
                            load(feedItem.getUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(holder.feedImageView);
                } catch (Exception e) {
                    holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case VIDEO: {
                mediaPlayer.stop();
                holder.setVisibilities(View.GONE, View.VISIBLE, View.GONE);
                MediaController mediaController = new MediaController(activity);
                holder.capturedVideoView.setMediaController(mediaController);
                String uri = feedItem.getUrl();
                Uri videoUri = Uri.parse(uri);
                holder.capturedVideoView.setVideoURI(videoUri);
                holder.capturedVideoView.start();
                mediaController.show(5000);
                if (mediaController.isShowing()) {
                    mediaController.hide();
                }
                break;
            }
            case ROOT_COMMENT: {
                mediaPlayer.stop();
                holder.setVisibilities(View.GONE, View.GONE, View.VISIBLE);
                String comment = feedItem.getTitle().replaceAll("^\"|\"$", "");

                holder.feedTextView.setBackground(getCommentColor(comment));
                holder.feedTextView.setText(getCommentText(comment));

            }
        }
        holder.likeImageView.setOnClickListener(v -> {
            if (isBoardActive) {
                if (feedsListItem.get(position).getFeedInteractions().getHasLiked()) {
                    boardFeedItemDeleteLike(feedId, holder);
                } else {
                    boardFeedItemLikeApiCall(feedId, holder);
                }
            } else {
                displaySnackBar(holder.likeImageView, R.string.board_not_active_message);
            }
        });

        holder.shareImageView.setOnClickListener(v -> {
            if (isBoardActive) {
                // TODO: Make api call to share
            } else {
                displaySnackBar(holder.likeImageView, R.string.board_not_active_message);
            }
        });

        holder.dislikeImageView.setOnClickListener(v -> {
            if (isBoardActive) {
                if (feedsListItem.get(position).getFeedInteractions().getHasDisliked()) {
                    boardFeedItemDeleteDisLike(feedId, holder);
                } else {
                    boardFeedItemDisLikeApiCall(feedId, holder);
                }
            } else {
                displaySnackBar(holder.likeImageView, R.string.board_not_active_message);
            }
        });

        holder.feedTitleTextView.setOnClickListener(view -> {
            holder.scrollView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.scrollView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.feedDescription.setText(feedItem.getDescription());
        });

        holder.pinImageView.setOnClickListener(view -> {
            if (feedEntry.getFeedInteractions().getHasPinned()) {
                unpinItem(feedsListItem.get(position), position);
            } else {
                pinItem(feedsListItem.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedsListItem.size();
    }

    public void update(List<FeedEntry> feedEntryList) {
        this.feedsListItem = feedEntryList;
        notifyDataSetChanged();
    }

    public void updateNewFeed(FeedEntry feedEntry) {
        if (isNullOrEmpty(feedsListItem)) {
            feedsListItem = new ArrayList<>();
        }
        feedsListItem.add(0, feedEntry);
        notifyItemInserted(0);
    }


    private void boardFeedItemLikeApiCall(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.postLike(feedItemId, boardId, target, getRequestDateStringOfNow(), getToken())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                int tint = ContextCompat.getColor(activity, R.color.frog_green);
                                holder.likeImageView.setImageTintList(ColorStateList.valueOf(tint));
                                holder.dislikeImageView.setVisibility(View.INVISIBLE);
                                holder.likeCountTextView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.INVISIBLE);
                                if (target != null) {
                                    retrieveBoardById();
                                } else {
                                    retrieveFeedItems();
                                }
                                break;
                            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                Toast.makeText(activity, "You have already liked the item", Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(activity, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void retrieveFeedItems() {
        Observable<Response<List<FeedEntry>>> userFeedApiCall = isNullOrEmpty(getToken()) ? restApi.getUserFeed() : restApi.getUserFeed(getToken());
        userFeedApiCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<FeedEntry>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FeedEntry>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<FeedEntry> feedEntries = response.body();
                                if (feedEntries != null) {
                                    update(feedEntries);
                                } else
                                    Toast.makeText(activity, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(activity, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    private void retrieveBoardById() {

        restApi.getBoardFeedItems(boardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<FeedEntry>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error()" + e);
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FeedEntry>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(activity, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void boardFeedItemDeleteLike(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.deleteLike(feedItemId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                holder.likeImageView.setImageTintList(null);
                                holder.likeCountTextView.setVisibility(View.INVISIBLE);
                                holder.dislikeImageView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Toast.makeText(activity, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void boardFeedItemDisLikeApiCall(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.postDisLike(feedItemId, boardId, target, getRequestDateStringOfNow(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                int tint = ContextCompat.getColor(activity, R.color.salmon);
                                holder.dislikeImageView.setImageTintList(ColorStateList.valueOf(tint));
                                holder.likeImageView.setVisibility(View.INVISIBLE);
                                holder.dislikeCountTextView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.INVISIBLE);
                                retrieveBoardById();
                                break;
                            default:
                                Toast.makeText(activity, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }


    private void boardFeedItemDeleteDisLike(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.deleteDisLike(feedItemId, getToken())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:

                                holder.dislikeImageView.setImageTintList(null);
                                holder.dislikeCountTextView.setVisibility(View.INVISIBLE);
                                holder.likeImageView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Toast.makeText(activity, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    private void pinItem(FeedEntry feedEntry, int position) {
        restApi.pinFeedItem(feedEntry.getFeedItem().getId(), BOARD, boardId, getRequestDateStringOfNow(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : pinItem()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "pinItem() " + e.getMessage());
                        Toast.makeText(activity, R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_CREATED:
                                feedEntry.getFeedInteractions().setHasPinned(true);
                                feedEntry.getFeedInteractions().setPinId(response.body());
                                feedEntry.getFeedInteractions().setPreviousPosition(position);
                                toggleFeedItemPin(feedEntry, true);
                                pinFeedEntry(feedsListItem, feedEntry);
                                notifyItemChanged(position);
                                notifyItemMoved(position, 0);
                                activity.moveItem(position, 0);
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(activity, R.string.failed_to_find_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HTTP_INTERNAL_ERROR:
                                Toast.makeText(activity, R.string.already_pinned_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void unpinItem(FeedEntry feedEntry, int position) {
        restApi.unpinFeedItem(boardId, feedEntry.getFeedInteractions().getPinId(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : unpinItem()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "unpinItem() " + e.getMessage());
                        Toast.makeText(activity, R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_NO_CONTENT:
                                feedEntry.getFeedInteractions().setHasPinned(false);
                                feedEntry.getFeedInteractions().setPinId(null);
                                toggleFeedItemPin(feedEntry, false);
                                unpinFeedEntry(feedsListItem, feedEntry);
                                notifyItemChanged(position);
                                notifyItemMoved(position, feedEntry.getFeedInteractions().getPreviousPosition());
                                activity.moveItem(position, feedEntry.getFeedInteractions().getPreviousPosition());
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(activity, R.string.failed_to_find_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HTTP_INTERNAL_ERROR:
                                Toast.makeText(activity, R.string.already_removed_pin, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, R.string.failed_to_unpin_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    static class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_pic)
        ImageView profilePic;
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.feed_top_layout)
        LinearLayout feedTopLayout;
        @BindView(R.id.like_image_view)
        ImageView likeImageView;
        @BindView(R.id.pin_image_view)
        ImageView pinImageView;
        @BindView(R.id.share_image_view)
        ImageView shareImageView;
        @BindView(R.id.dislike_image_view)
        ImageView dislikeImageView;
        @BindView(R.id.captured_video_view)
        VideoView capturedVideoView;
        @BindView(R.id.feed_text_view)
        TextView feedTextView;
        @BindView(R.id.drag_handle)
        ImageView dragHandleImageView;
        @BindView(R.id.user_name_text_view)
        TextView userNameTextView;
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.feed_description)
        TextView feedDescription;
        @BindView(R.id.scroll_view)
        ScrollView scrollView;
        @BindView(R.id.like_count)
        TextView likeCountTextView;
        @BindView(R.id.dislike_count)
        TextView dislikeCountTextView;
        @BindView(R.id.like_separator)
        View likeSeparator;

        FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setVisibilities(int imageViewVisibility, int videoViewVisibility, int textViewVisibility) {
            feedImageView.setVisibility(imageViewVisibility);
            capturedVideoView.setVisibility(videoViewVisibility);
            feedTextView.setVisibility(textViewVisibility);
        }
    }
}
