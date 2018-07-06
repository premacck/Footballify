package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class BoardFeedDetailAdapter extends RecyclerView.Adapter<BoardFeedDetailAdapter.FootballFeedDetailViewHolder> {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    private String boardId;
    private List<FootballFeed> footballFeedsList = new ArrayList<>();
    private RestApi restApi;
    private Context context;
    private String objectId;
    private int likeCount = 0;

    public BoardFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList, String boardId) {
        this.context = context;
        this.footballFeedsList = footballFeedsList;
        this.boardId = boardId;
    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ((ZoneApplication) getApplication()).getBoardItemLikeNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_feed_detail_row, parent, false);
        return new FootballFeedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(context);
        String feedId = footballFeedsList.get(position).getId();
        objectId = preference.getString(context.getString(R.string.object_id_string), "NA");
        if (footballFeedsList.get(position).getThumbnail() != null) {
            switch (footballFeedsList.get(position).getContentType()) {
                case "Image": {
                    holder.feedImageView.setVisibility(View.VISIBLE);
                    holder.feedTextView.setVisibility(View.INVISIBLE);
                    holder.feedTitleTextView.setText(footballFeedsList.get(position).getTitle());
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
                    holder.feedTextView.setVisibility(View.INVISIBLE);
                    holder.feedImageView.setVisibility(View.VISIBLE);
                    holder.feedImageView.setImageResource(R.drawable.ic_audio);
                    holder.feedTitleTextView.setText(footballFeedsList.get(position).getTitle());
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

                default: {
                    try {
                        Picasso.with(context).
                                load(R.drawable.football_image_one)
                                .error(R.drawable.ic_place_holder)
                                .placeholder(R.drawable.ic_place_holder)
                                .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(30, context), 0))
                                .into(holder.profileImageView);
                    } catch (Exception e) {
                        holder.profileImageView.setImageResource(R.drawable.ic_place_holder);
                    }
                    break;
                }
            }
        } else {
            if (footballFeedsList.get(position).getContentType().equals("rootComment")) {
                holder.feedTextView.setVisibility(View.VISIBLE);
                holder.capturedVideoView.setVisibility(View.INVISIBLE);

                String comment = footballFeedsList.get(position).getTitle().replaceAll("^\"|\"$", "");
                holder.feedImageView.setBackgroundColor(context.getResources().getColor(Integer.parseInt(comment.substring(0, comment.indexOf("$")))));
                holder.feedTextView.setText(comment.substring(comment.indexOf("$") + 1));
            }
        }

        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemLikeApiCall(feedId, objectId);
                //todo: remove static like count and add from Board Feed Interactions
                likeCount++;
                holder.likeCountTextView.setText(String.valueOf(likeCount));
                holder.likeCountTextView.setTextColor(context.getResources().getColor(R.color.text_hint_label_color));
            }
        });

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemShareApiCall(feedId, AppConstants.SHARE_TO, boardId, objectId);
            }
        });

        holder.unlikeCountImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardDislikeFeedItem(feedId, objectId);
                //todo:will replace with getFoorballFeed Api call original Like counts
                if (likeCount == 0) {
                    holder.likeCountTextView.setText(String.valueOf(likeCount));
                } else {
                    holder.likeCountTextView.setText(String.valueOf(--likeCount));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    private void boardFeedItemLikeApiCall(String id, String userId) {
        restApi.getLikedFeedItem(id, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FootballFeed>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<FootballFeed> feedItemModelResponse) {
                        Log.e("", "onNext: " + feedItemModelResponse);
                    }
                });

    }

    private void boardFeedItemShareApiCall(String id, String shareTo, String boardId, String userId) {
        restApi.shareBoardFeedItem(id, shareTo, boardId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FootballFeed>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<FootballFeed> feedItemModelResponse) {
                        Log.e("", "onNext: " + feedItemModelResponse);
                        Toast.makeText(context, R.string.share_feed_item_toast, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void boardDislikeFeedItem(String id, String userId) {
        restApi.dislikeBoardItem(id, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        Log.e("", "onNext: " + jsonObjectResponse);
                    }
                });

    }
    //todo:make a call to get dislike count.

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.profile_image_view)
        ImageView profileImageView;
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

        FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
