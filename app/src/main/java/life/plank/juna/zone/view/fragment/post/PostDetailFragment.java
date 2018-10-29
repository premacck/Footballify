package life.plank.juna.zone.view.fragment.post;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
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
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.data.model.FeedItemCommentReply;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.util.customview.ShimmerRelativeLayout;
import life.plank.juna.zone.view.adapter.EmojiAdapter;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
import static life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.getBoldText;
import static life.plank.juna.zone.util.UIDisplayUtil.getClickableLink;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getCommentText;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;
import static life.plank.juna.zone.util.UIDisplayUtil.hideSoftKeyboard;

public class PostDetailFragment extends Fragment implements FeedInteractionListener {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    @BindView(R.id.comments_shimmer)
    ShimmerRelativeLayout commentsShimmer;
    @BindView(R.id.no_comment_text_view)
    TextView noCommentTextView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_comments_list)
    RecyclerView postCommentsRecyclerView;
    @BindView(R.id.post_comment_layout)
    RelativeLayout postCommentLayout;

    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.feed_content)
    ShimmerRelativeLayout feedContentLayout;
    @BindView(R.id.feed_image_view)
    ImageView feedImageView;
    @BindView(R.id.pin_image_view)
    ImageView pinImageView;
    @BindView(R.id.share_image_view)
    ImageView shareImageView;
    @BindView(R.id.captured_video_view)
    VideoView capturedVideoView;
    @BindView(R.id.feed_text_view)
    TextView feedTextView;
    @BindView(R.id.user_name_text_view)
    TextView userNameTextView;
    @BindView(R.id.feed_title_text_view)
    TextView feedTitleTextView;
    @BindView(R.id.description_text_view)
    TextView feedDescription;
    @BindView(R.id.emoji_bottom_sheet)
    RelativeLayout emojiBottomSheet;
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    @BindView(R.id.emoji_recycler_view)
    RecyclerView emojiRecyclerView;
    @BindView(R.id.reaction_view)
    ImageView reactionView;

    @Inject
    @Named("default")
    RestApi restApi;

    private FeedEntry feedEntry;
    private String boardId;
    private PostCommentAdapter adapter;
    private BottomSheetBehavior emojiBottomSheetBehavior;
    private EmojiAdapter emojiAdapter;

    public static PostDetailFragment newInstance(@NonNull FeedEntry feedEntry, String boardId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ZoneApplication.getContext().getString(R.string.intent_feed_items), feedEntry);
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
            feedEntry = args.getParcelable(getString(R.string.intent_feed_items));
            boardId = args.getString(getString(R.string.intent_board_id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new PostCommentAdapter(Glide.with(this), this, "");
        postCommentsRecyclerView.setAdapter(adapter);
        setupBottomSheet();
        initBottomSheetRecyclerView();
        return rootView;
    }

    private void initBottomSheetRecyclerView() {
        emojiAdapter = new EmojiAdapter(getActivity(), boardId, emojiBottomSheetBehavior);
        emojiRecyclerView.setAdapter(emojiAdapter);
        EmojiAdapter.feedId = feedEntry.getFeedItem().getId();
    }

    private void setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emojiBottomSheet);
        emojiBottomSheetBehavior.setPeekHeight(0);
        emojiBottomSheetBehavior.setHideable(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bindFeetContent();
        getCommentsOnFeed(false);
        swipeRefreshLayout.setOnRefreshListener(() -> getCommentsOnFeed(true));
    }

    @Override
    public void onResume() {
        super.onResume();
        feedTitleTextView.setSelected(true);
    }

    private void bindFeetContent() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        if (feedEntry.getFeedItem().getUser() != null) {
            Glide.with(this)
                    .load(feedEntry.getFeedItem().getUser().getProfilePictureUrl())
                    .apply(RequestOptions.centerInsideTransform().override((int) getDp(20), (int) getDp(20)))
                    .into(profilePic);
        }

        if (feedEntry.getFeedItem().getUser() != null) {
            userNameTextView.setText(feedEntry.getFeedItem().getUser().getDisplayName());
        } else {
            SharedPreferences userPref = Objects.requireNonNull(getActivity()).getSharedPreferences(getActivity().getString(R.string.pref_login_credentails), 0);
            String userEmailId = userPref.getString(getActivity().getString(R.string.pref_email_address), "NA");
            userNameTextView.setText(userEmailId);
        }
        feedTitleTextView.setText(!feedEntry.getFeedItem().getContentType().equals(ROOT_COMMENT) ? feedEntry.getFeedItem().getTitle() : null);
        if (feedEntry.getFeedItem().getContentType().equals(NEWS)) {
            feedDescription.setVisibility(VISIBLE);
            feedDescription.setMovementMethod(LinkMovementMethod.getInstance());
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder()
                    .append(getBoldText(feedEntry.getFeedItem().getSource()))
                    .append("\n\n")
                    .append(feedEntry.getFeedItem().getSummary())
                    .append("\n\n")
                    .append(getClickableLink(getActivity(), feedEntry.getFeedItem().getUrl()));
            feedDescription.setText(stringBuilder);
        } else {
            feedDescription.setVisibility(feedEntry.getFeedItem().getDescription() == null ? GONE : VISIBLE);
            feedDescription.setText(feedEntry.getFeedItem().getDescription());
        }

        feedEntry.getFeedInteractions();
        pinImageView.setImageResource(
                feedEntry.getFeedInteractions().getHasPinned() ?
                        R.drawable.ic_pin_active :
                        R.drawable.ic_pin_inactive
        );

        switch (feedEntry.getFeedItem().getContentType()) {
            case NEWS:
            case IMAGE:
                mediaPlayer.stop();
                setVisibilities(VISIBLE, GONE, GONE);
                try {
                    String urlToLoad = feedEntry.getFeedItem().getContentType().equals(NEWS) ?
                            feedEntry.getFeedItem().getThumbnail().getImageUrl() :
                            feedEntry.getFeedItem().getUrl();
                    feedContentLayout.startShimmerAnimation();
                    Glide.with(this).asBitmap().load(urlToLoad)
                            .apply(RequestOptions.errorOf(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    if (getActivity() == null) {
                                        return;
                                    }
                                    int width = (int) (getScreenSize(getActivity().getWindowManager().getDefaultDisplay())[0] - getDp(8));
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) feedImageView.getLayoutParams();
                                    params.height = width * bitmap.getHeight() / bitmap.getWidth();
                                    feedImageView.setLayoutParams(params);
                                    feedContentLayout.stopShimmerAnimation();
                                    feedImageView.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    feedContentLayout.stopShimmerAnimation();
                                    feedImageView.setImageDrawable(errorDrawable);
                                }
                            });
                } catch (Exception e) {
                    feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            case AUDIO:
                mediaPlayer.stop();
                setVisibilities(VISIBLE, GONE, GONE);
                feedImageView.setImageResource(R.drawable.ic_mic_white);

                String audioUriString = feedEntry.getFeedItem().getUrl();
                Uri audioUri = Uri.parse(audioUriString);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(ZoneApplication.getContext(), audioUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    mediaPlayer.stop();
                }

                mediaPlayer.start();

                try {
                    Picasso.with(getActivity()).
                            load(feedEntry.getFeedItem().getUrl())
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(feedImageView);
                } catch (Exception e) {
                    feedImageView.setImageResource(R.drawable.ic_place_holder);
                }
                break;
            case VIDEO:
                mediaPlayer.stop();
                setVisibilities(GONE, VISIBLE, GONE);
                String videoUriString = feedEntry.getFeedItem().getUrl();
                Uri videoUri = Uri.parse(videoUriString);
                capturedVideoView.setVideoURI(videoUri);
                capturedVideoView.start();
                break;
            case ROOT_COMMENT:
                mediaPlayer.stop();
                setVisibilities(GONE, GONE, VISIBLE);
                String comment = feedEntry.getFeedItem().getTitle().replaceAll("^\"|\"$", "");

                feedTextView.setBackground(getCommentColor(comment));
                feedTextView.setText(getCommentText(comment));
                break;
        }

    }

    private void pinItem() {
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
                        Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_CREATED:
                                feedEntry.getFeedInteractions().setHasPinned(false);
                                feedEntry.getFeedInteractions().setPinId(response.body());
                                pinImageView.setImageResource(R.drawable.ic_pin_active);
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_find_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HTTP_INTERNAL_ERROR:
                                Toast.makeText(ZoneApplication.getContext(), R.string.already_pinned_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void unpinItem() {
        if (feedEntry.getFeedInteractions().getPinId() == null) {
            feedEntry.getFeedInteractions().setHasPinned(false);
            pinImageView.setImageResource(R.drawable.ic_pin_inactive);
            return;
        }
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
                        Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_pin_feed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_NO_CONTENT:
                                feedEntry.getFeedInteractions().setHasPinned(false);
                                feedEntry.getFeedInteractions().setPinId(null);
                                pinImageView.setImageResource(R.drawable.ic_pin_inactive);
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_find_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HTTP_INTERNAL_ERROR:
                                Toast.makeText(ZoneApplication.getContext(), R.string.already_removed_pin, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_unpin_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @OnClick(R.id.pin_image_view)
    public void onPinClick() {
        if (feedEntry.getFeedInteractions().getHasPinned()) {
            unpinItem();
        } else {
            pinItem();
        }
    }

    @OnClick(R.id.reaction_view)
    public void onReact() {
        emojiBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        emojiBottomSheetBehavior.setPeekHeight(850);
        EmojiAdapter.feedId = feedEntry.getFeedItem().getId();
        emojiBottomSheetBehavior.setPeekHeight(850);
    }
    //endregion

    private void setVisibilities(int imageViewVisibility, int videoViewVisibility, int textViewVisibility) {
        feedImageView.setVisibility(imageViewVisibility);
        capturedVideoView.setVisibility(videoViewVisibility);
        feedTextView.setVisibility(textViewVisibility);
    }

    private void getCommentsOnFeed(boolean isRefreshing) {
        if (isNullOrEmpty(getToken())) {
            updateCommentsUi(GONE, GONE, VISIBLE, R.string.login_signup_to_view_comments);
            postCommentLayout.setVisibility(GONE);
        }

        restApi.getCommentsForFeed(feedEntry.getFeedItem().getId(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(true);
                    updateCommentsUi(VISIBLE, GONE, GONE, 0);
                })
                .doOnTerminate(() -> {
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(new Subscriber<Response<List<FeedItemComment>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : getCommentsForFeed(" + feedEntry + ")");
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
                                feedEntry.getFeedItem().setComments(commentList);
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
        restApi.postCommentOnFeedItem(commentEditText.getText().toString(), feedEntry.getFeedItem().getId(), boardId, getRequestDateStringOfNow(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FeedItemComment>>() {
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
                    public void onNext(Response<FeedItemComment> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_CREATED:
                                commentEditText.setText(null);
                                adapter.addComment(response.body());
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
            getCommentsOnFeed(false);
        }
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
        restApi.postReplyOnComment(reply, feedEntry.getFeedItem().getId(), comment.getId(), boardId, getRequestDateStringOfNow(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FeedItemCommentReply>>() {
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
                    public void onNext(Response<FeedItemCommentReply> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                            case HTTP_CREATED:
                                if (isNullOrEmpty(comment.getReplies())) {
                                    comment.setReplies(new ArrayList<>());
                                }
                                FeedItemCommentReply commentReply = response.body();
                                if (commentReply != null) {
                                    comment.getReplies().add(0, commentReply);
                                    adapter.onReplyPostedOnComment(position, comment);
                                }
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