package life.plank.juna.zone.view.adapter;

import android.content.Context;
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
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.ColorHashMap;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

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
    private List<FootballFeed> footballFeedsList;
    private RestApi restApi;
    private Context context;
    private String date;
    private String boardId;

    public BoardFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList, String boardId) {
        ColorHashMap.HashMaps(context);
        this.context = context;
        this.footballFeedsList = footballFeedsList;
    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        getApplication().getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_detail_row, parent, false);
        return new FootballFeedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {

        date = new SimpleDateFormat(context.getString(R.string.string_format)).format(Calendar.getInstance().getTime());

        if (footballFeedsList.get(position).getInteractions() != null) {
            holder.likeCountTextView.setText(String.valueOf(footballFeedsList.get(position).getInteractions().getLikes()));
            holder.dislikeCountTextView.setText(String.valueOf(footballFeedsList.get(position).getInteractions().getDislikes()));
        }

        SharedPreferences matchPref = context.getSharedPreferences(context.getString(R.string.pref_enter_board_id), 0);
        boardId = matchPref.getString(context.getString(R.string.pref_enter_board_id), "NA");
        String feedId = footballFeedsList.get(position).getId();
        if (footballFeedsList.get(position).getActor() != null) {
            holder.userNameTextView.setText(footballFeedsList.get(position).getActor().getDisplayName());
        } else {
            SharedPreferences userPref = context.getSharedPreferences(context.getString(R.string.pref_login_credentails), 0);
            String userEmailId = userPref.getString(context.getString(R.string.pref_email_address), "NA");
            holder.userNameTextView.setText(userEmailId);
        }
        holder.feedTitleTextView.setText(footballFeedsList.get(position).getDescription());
        setupSwipeGesture(context, holder.dragHandleImageView);

        switch (footballFeedsList.get(position).getContentType()) {
            case "Image": {
                mediaPlayer.stop();
                holder.feedImageView.setVisibility(View.VISIBLE);
                holder.feedTextView.setVisibility(View.INVISIBLE);

                try {
                    Picasso.with(context).
                            load(footballFeedsList.get(position).getThumbnail().getImageUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(holder.feedImageView);
                } catch (Exception e) {
                    holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case "Audio": {
                mediaPlayer.stop();
                holder.feedTextView.setVisibility(View.INVISIBLE);
                holder.feedImageView.setVisibility(View.VISIBLE);
                holder.feedImageView.setImageResource(R.drawable.ic_audio);

                String uri = footballFeedsList.get(position).getUrl();
                Uri videoUri = Uri.parse(uri);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(context, videoUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    mediaPlayer.stop();
                }

                mediaPlayer.start();

                try {
                    Picasso.with(context).
                            load(footballFeedsList.get(position).getUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(holder.feedImageView);
                } catch (Exception e) {
                    holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case "Video": {
                mediaPlayer.stop();
                holder.feedTextView.setVisibility(View.INVISIBLE);
                holder.feedImageView.setVisibility(View.INVISIBLE);
                holder.capturedVideoView.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(context);
                holder.capturedVideoView.setMediaController(mediaController);
                String uri = footballFeedsList.get(position).getUrl();
                Uri videoUri = Uri.parse(uri);
                holder.capturedVideoView.setVideoURI(videoUri);
                holder.capturedVideoView.start();
                mediaController.show(5000);
                if (mediaController.isShowing()) {
                    mediaController.hide();
                }
                break;
            }
            case "rootComment": {
                mediaPlayer.stop();
                holder.feedTextView.setVisibility(View.VISIBLE);
                holder.feedImageView.setVisibility(View.INVISIBLE);
                holder.capturedVideoView.setVisibility(View.INVISIBLE);
                String comment = footballFeedsList.get(position).getTitle().replaceAll("^\"|\"$", "");

                holder.commentBg.setBackgroundColor(getCommentColor(comment));
                holder.feedTextView.setText(getCommentText(comment));
            }
        }
        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemLikeApiCall(feedId, date, holder, position);
            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Make api call to share
            }
        });

        holder.dislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemDisLikeApiCall(feedId, date, holder, position);
            }
        });

        holder.feedTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.scrollView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.scrollView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                holder.feedDescription.setText(footballFeedsList.get(position).getDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    private void boardFeedItemLikeApiCall(String feedItemId, String dateCreated, FootballFeedDetailViewHolder holder, int position) {
        restApi.postLike(feedItemId, boardId, "Boards", dateCreated, getToken(context))
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
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                int tint = ContextCompat.getColor(context, R.color.frog_green);
                                holder.likeImageView.setImageTintList(ColorStateList.valueOf(tint));
                                holder.dislikeImageView.setVisibility(View.INVISIBLE);
                                holder.likeCountTextView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.INVISIBLE);
                                retrieveBoardById(holder, position);
                                break;
                            default:
                                Toast.makeText(context, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void retrieveBoardById(FootballFeedDetailViewHolder holder, int position) {

        restApi.retrieveByBoardId(boardId, getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error()" + e);
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                footballFeedsList = response.body();
                                notifyDataSetChanged();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(context, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(context, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void boardFeedItemDeleteLike(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.deleteLike(feedItemId, getToken(context))
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
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                //TODO: Add logic to toggle the visibility of like count
                                break;
                            default:
                                Toast.makeText(context, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void boardFeedItemDisLikeApiCall(String feedItemId, String dateCreated, FootballFeedDetailViewHolder holder, int position) {
        restApi.postDisLike(feedItemId, boardId, "Boards", dateCreated, getToken(context))
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
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                int tint = ContextCompat.getColor(context, R.color.salmon);
                                holder.dislikeImageView.setImageTintList(ColorStateList.valueOf(tint));
                                holder.likeImageView.setVisibility(View.INVISIBLE);
                                holder.dislikeCountTextView.setVisibility(View.VISIBLE);
                                holder.likeSeparator.setVisibility(View.INVISIBLE);
                                retrieveBoardById(holder, position);
                                break;
                            default:
                                Toast.makeText(context, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    private void boardFeedItemDeleteDisLike(String feedItemId, FootballFeedDetailViewHolder holder) {
        restApi.deleteDisLike(feedItemId, getToken(context))
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
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                //TODO: update dislike count
                                break;
                            default:
                                Toast.makeText(context, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.comment_bg)
        TextView commentBg;

        @BindView(R.id.like_image_view)
        ImageView likeImageView;
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
    }
}
