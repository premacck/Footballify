package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
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
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.ColorHashMap;
import retrofit2.Response;
import retrofit2.Retrofit;
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
    private String TAG = BoardFeedDetailAdapter.class.getCanonicalName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;

    MediaPlayer mediaPlayer = new MediaPlayer();
    private String boardId;
    private List<FootballFeed> footballFeedsList;
    private RestApi restApi;
    private Context context;

    private int likeCount;
    private String date;
    private String enterBoardId;
    ;

    public BoardFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList, String boardId) {
        ColorHashMap.HashMaps(context);
        this.context = context;
        this.footballFeedsList = footballFeedsList;
        this.boardId = boardId;
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
        SharedPreferences matchPref = context.getSharedPreferences(context.getString(R.string.pref_enter_board_id), 0);
        enterBoardId = matchPref.getString(context.getString(R.string.pref_enter_board_id), "NA");
        String feedId = footballFeedsList.get(position).getId();

        setupSwipeGesture(context, holder.dragHandleImageView);
        if (footballFeedsList.get(position).getInteractions() != null) {
            likeCount = footballFeedsList.get(position).getInteractions().getLikes();
        } else {
            likeCount = 0;
        }

        holder.likeCountTextView.setText(Integer.toString(likeCount));

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
                boardFeedItemLikeApiCall(feedId, enterBoardId, date, holder);
            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Make api call to share
            }
        });

        holder.unlikeCountImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemDisLikeApiCall(feedId, enterBoardId, date, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    private void boardFeedItemLikeApiCall(String feedItemId, String boardId, String dateCreated, FootballFeedDetailViewHolder holder) {
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
                                holder.likeCountTextView.setText(Integer.toString(++likeCount));
                                break;
                            default:
                                Toast.makeText(context, R.string.like_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void boardFeedItemDisLikeApiCall(String feedItemId, String boardId, String dateCreated, FootballFeedDetailViewHolder holder) {
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
        @BindView(R.id.number_of_likes_text_view)
        TextView likeCountTextView;
        @BindView(R.id.unlike_image_view)
        ImageView unlikeCountImageView;
        @BindView(R.id.captured_video_view)
        VideoView capturedVideoView;
        @BindView(R.id.feed_text_view)
        TextView feedTextView;
        @BindView(R.id.drag_handle)
        ImageView dragHandleImageView;

        FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
