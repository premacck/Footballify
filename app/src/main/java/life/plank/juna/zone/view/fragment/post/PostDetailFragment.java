package life.plank.juna.zone.view.fragment.post;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FeedItem;
import life.plank.juna.zone.data.network.model.FeedItemComment;
import life.plank.juna.zone.data.network.model.FeedItemCommentReply;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;
import static life.plank.juna.zone.util.UIDisplayUtil.hideSoftKeyboard;

public class PostDetailFragment extends Fragment implements FeedInteractionListener {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    @BindView(R.id.comments_shimmer)
    ShimmerRelativeLayout commentsShimmer;
    @BindView(R.id.no_comment_text_view)
    TextView noCommentTextView;
    @BindView(R.id.post_comments_list)
    RecyclerView postCommentsRecyclerView;

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
    @BindView(R.id.description_text_view)
    TextView feedDescription;
    @BindView(R.id.like_count)
    TextView likeCountTextView;
    @BindView(R.id.dislike_count)
    TextView dislikeCountTextView;
    @BindView(R.id.like_separator)
    View likeSeparator;

    @BindView(R.id.comment_edit_text)
    EditText commentEditText;

    @Inject
    Gson gson;
    @Inject
    Picasso picasso;
    @Inject
    @Named("default")
    RestApi restApi;

    private FeedItem feedItem;
    private String boardId;
    private PostCommentAdapter adapter;

    public static PostDetailFragment newInstance(@NonNull String feedItemString, String boardId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString(ZoneApplication.getContext().getString(R.string.intent_feed_items), feedItemString);
        args.putString(ZoneApplication.getContext().getString(R.string.intent_board_id), boardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().getUiComponent().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            feedItem = gson.fromJson(args.getString(getString(R.string.intent_feed_items)), FeedItem.class);
            boardId = args.getString(getString(R.string.intent_board_id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new PostCommentAdapter(picasso, this);
        postCommentsRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bindFeetContent();
        getCommentsOnFeed();
    }

    private void bindFeetContent() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        if (feedItem.getInteractions() != null) {
            likeCountTextView.setText(String.valueOf(feedItem.getInteractions().getLikes()));
            dislikeCountTextView.setText(String.valueOf(feedItem.getInteractions().getDislikes()));
        }

        if (feedItem.getActor() != null) {
            userNameTextView.setText(feedItem.getActor().getDisplayName());
        } else {
            SharedPreferences userPref = Objects.requireNonNull(getActivity()).getSharedPreferences(getActivity().getString(R.string.pref_login_credentails), 0);
            String userEmailId = userPref.getString(getActivity().getString(R.string.pref_email_address), "NA");
            userNameTextView.setText(userEmailId);
        }
        feedTitleTextView.setText(feedItem.getDescription());

        pinImageView.setImageResource(
                feedItem.isPinned() ?
                        R.drawable.ic_pin_active :
                        R.drawable.ic_pin_inactive
        );

        switch (feedItem.getContentType()) {
            case "Image": {
                mediaPlayer.stop();
                setVisibilities(View.VISIBLE, View.GONE, View.GONE);
                try {
                    Picasso.with(getActivity()).
                            load(feedItem.getThumbnail().getImageUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(feedImageView);
                } catch (Exception e) {
                    feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case "Audio": {
                mediaPlayer.stop();
                setVisibilities(View.VISIBLE, View.GONE, View.GONE);
                feedImageView.setImageResource(R.drawable.ic_audio);

                String uri = feedItem.getUrl();
                Uri videoUri = Uri.parse(uri);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(getActivity(), videoUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    mediaPlayer.stop();
                }

                mediaPlayer.start();

                try {
                    Picasso.with(getActivity()).
                            load(feedItem.getUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(feedImageView);
                } catch (Exception e) {
                    feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            }
            case "Video": {
                mediaPlayer.stop();
                setVisibilities(View.GONE, View.VISIBLE, View.GONE);
                MediaController mediaController = new MediaController(getActivity());
                capturedVideoView.setMediaController(mediaController);
                String uri = feedItem.getUrl();
                Uri videoUri = Uri.parse(uri);
                capturedVideoView.setVideoURI(videoUri);
                capturedVideoView.start();
                mediaController.show(5000);
                if (mediaController.isShowing()) {
                    mediaController.hide();
                }
                break;
            }
            case "rootComment": {
                mediaPlayer.stop();
                setVisibilities(View.GONE, View.GONE, View.VISIBLE);
                String comment = feedItem.getTitle().replaceAll("^\"|\"$", "");

                feedTextView.setBackground(getCommentColor(comment));
                feedTextView.setText(getCommentText(comment));
            }
        }
    }

    private void setVisibilities(int imageViewVisibility, int videoViewVisibility, int textViewVisibility) {
        feedImageView.setVisibility(imageViewVisibility);
        capturedVideoView.setVisibility(videoViewVisibility);
        feedTextView.setVisibility(textViewVisibility);
    }

    private void getCommentsOnFeed() {
        restApi.getCommentsForFeed(feedItem.getId(), getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> updateCommentsUi(VISIBLE, GONE, GONE, 0))
                .subscribe(new Subscriber<Response<List<FeedItemComment>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : getCommentsForFeed(" + feedItem + ")");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        updateCommentsUi(GONE, GONE, VISIBLE, R.string.failed_to_get_feed_comments);
                    }

                    @Override
                    public void onNext(Response<List<FeedItemComment>> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                                updateCommentsUi(GONE, VISIBLE, GONE, 0);
                                List<FeedItemComment> commentList = response.body();
                                feedItem.setComments(commentList);
                                adapter.setComments(commentList);
                                break;
                            case HTTP_NOT_FOUND:
                                updateCommentsUi(GONE, GONE, VISIBLE, R.string.be_the_first_to_comment_on_this_post);
                                break;
                            default:
                                updateCommentsUi(GONE, GONE, VISIBLE, R.string.failed_to_get_feed_comments);
                                break;
                        }
                    }
                });
    }

    private void updateCommentsUi(int shimmerVisibility, int recyclerViewVisibility, int errorMessageVisibility, @StringRes int message) {
        commentsShimmer.setVisibility(shimmerVisibility);
        postCommentsRecyclerView.setVisibility(recyclerViewVisibility);
        noCommentTextView.setVisibility(errorMessageVisibility);
        if (errorMessageVisibility == VISIBLE) {
            noCommentTextView.setText(message);
        }
        if (shimmerVisibility == VISIBLE) {
            commentsShimmer.startShimmerAnimation();
        } else {
            commentsShimmer.stopShimmerAnimation();
        }
    }

    @OnClick(R.id.post_comment)
    public void postComment() {
        if (commentEditText.getText().toString().isEmpty()) {
            commentEditText.setError(getString(R.string.please_enter_comment), getResources().getDrawable(R.drawable.ic_error, null));
            return;
        }

        commentEditText.clearFocus();
        hideSoftKeyboard(commentEditText);
        restApi.postCommentOnFeedItem(commentEditText.getText().toString(), feedItem.getId(), boardId, getRequestDateStringOfNow(), getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : postComment()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_comment, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        switch (response.code()) {
                            case HTTP_CREATED:
                                commentEditText.setText(null);
                                getCommentsOnFeed();
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_comment, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_comment, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @OnClick(R.id.no_comment_text_view)
    public void retryGettingComments() {
        if (noCommentTextView.getText().toString().equals(getString(R.string.failed_to_get_feed_comments))) {
            getCommentsOnFeed();
        }
    }

    @Override
    public void onPostLiked() {
    }

    @Override
    public void onPostUndoLiked() {
    }

    @Override
    public void onPostDisliked() {
    }

    @Override
    public void onPostUndoDisliked() {
    }

    @Override
    public void onCommentLiked() {
    }

    @Override
    public void onCommentDisliked() {
    }

    @Override
    public void onPostCommentOnFeed() {
    }

    @Override
    public void onPostReplyOnComment(String reply, int position, FeedItemComment comment) {
        restApi.postReplyOnComment(reply, feedItem.getId(), comment.getId(), boardId, getRequestDateStringOfNow(), getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_reply, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        switch (response.code()) {
                            case HTTP_CREATED:
                                if (isNullOrEmpty(comment.getReplies())) {
                                    comment.setReplies(new ArrayList<>());
                                }
//                                TODO : update with the reply got from backend
                                comment.getReplies().add(0, new FeedItemCommentReply(reply));
                                adapter.onReplyPostedOnComment(position, comment);
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_reply, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_post_reply, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}