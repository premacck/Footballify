package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.AppConstants.BOARD;
import static life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.emoji;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    private static final String TAG = EmojiAdapter.class.getSimpleName();
    @Inject
    @Named("default")
    RestApi restApi;
    private Context context;
    private String boardId;
    public static String feedId;

    public EmojiAdapter(Context activity, String boardId) {
        this.context = activity;
        this.boardId = boardId;
    }

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        getApplication().getUiComponent().inject(this);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmojiViewHolder holder, int position) {
        holder.emojiView.setImageDrawable(context.getDrawable(emoji[position].getEmojiUrl()));
        holder.emojiView.setOnClickListener(view -> {
            postEmoji(feedId, boardId, emoji[position].getReaction());
        });
    }

    private void postEmoji(String feedItemId, String boardId, Integer reaction) {
        restApi.postReaction(feedItemId, boardId, BOARD, getRequestDateStringOfNow(), reaction, getToken())

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
                                //Dismiss bottomsheet
                                break;
                            default:
                                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return emoji.length;
    }

    static class EmojiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.emoji_view)
        ImageView emojiView;

        EmojiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

