package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    List<FootballFeed> footballFeedsList = new ArrayList<>();
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    String boardFeedItemId;
    RestApi restApi;
    SharedPreferences saveBoardItemData;
    private Context context;
    private String objectId;
    private int likeCount = 0;

    public BoardFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList) {
        this.context = context;
        this.footballFeedsList = footballFeedsList;
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

        saveBoardItemData = context.getSharedPreferences(context.getString(R.string.board_feed_item), 0);
        boardFeedItemId = saveBoardItemData.getString("id", "NA");
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(context);
        objectId = preference.getString(context.getString(R.string.object_id_string), "NA");
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
        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardFeedItemLikeApiCall(boardFeedItemId, objectId);
                likeCount = likeCount + 1;
                holder.likeCountTextView.setText(String.valueOf(likeCount));
                holder.likeCountTextView.setTextColor(context.getResources().getColor(R.color.text_hint_label_color));
            }
        });
        holder.unlikeCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardUnlikeFeedItem(boardFeedItemId, objectId);
                if (likeCount == 0) {
                    likeCount = 0;
                    holder.likeCountTextView.setText(String.valueOf(likeCount));
                } else {
                    likeCount = likeCount - 1;
                    holder.likeCountTextView.setText(String.valueOf(likeCount));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    public void boardFeedItemLikeApiCall(String id, String userId) {
        restApi.getLikedFeedItem(id, userId)
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


    public void boardUnlikeFeedItem(String id, String userId) {
        restApi.unlikeBoardItem(id, userId)
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

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.profile_image_view)
        ImageView profileImageView;
        @BindView(R.id.like_image_view)
        ImageView likeImageView;
        @BindView(R.id.number_of_likes_text_view)
        TextView likeCountTextView;
        @BindView(R.id.unlike_image_view)
        ImageView unlikeCountTextView;

        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}