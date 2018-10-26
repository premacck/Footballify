package life.plank.juna.zone.view.fragment.forum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;

public class ForumFragment extends Fragment implements FeedInteractionListener {

    private static String matchBoardId;
    @Inject
    Picasso picasso;
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.post_comments_list)
    RecyclerView postCommentsRecyclerView;
    @BindView(R.id.post_comment)
    TextView postComment;
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    String date;
    private String userId;
    private PostCommentAdapter adapter;
    private RestApi restApi;

    public static ForumFragment newInstance(String boardId) {
        ForumFragment fragment = new ForumFragment();
        matchBoardId = boardId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().getUiComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forum, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new PostCommentAdapter(picasso, this, getString(R.string.forum));
        postCommentsRecyclerView.setAdapter(adapter);
        setAdapterData();
        restApi = retrofit.create(RestApi.class);
        date = new SimpleDateFormat(getString(R.string.string_format)).format(Calendar.getInstance().getTime());
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(getContext());
        userId = preference.getString(getString(R.string.pref_object_id), getString(R.string.na));
        return rootView;
    }

    //TODO: Remove hard coded data after backend integration.
    public void setAdapterData() {

        FeedItemComment firstFeedItemComment = new FeedItemComment("1", getString(R.string.first_feed_comment),
                "ROFootball",
                null, 0, false, 3, null);

        FeedItemComment secondFeedItemComment = new FeedItemComment("2", getString(R.string.first_feed_comment),
                "ROFootball",
                null, 3, true, 3, null);

        List<FeedItemComment> commentList = new ArrayList<>();
        commentList.add(firstFeedItemComment);
        commentList.add(secondFeedItemComment);
        adapter.setComments(commentList);

    }

    @OnClick({R.id.post_comment})
    public void onViewClicked() {
        postCommentOnBoardFeed(commentEditText.getText().toString(), matchBoardId, AppConstants.ROOT_COMMENT, userId, date);
    }

    //TODO: Update url
    private void postCommentOnBoardFeed(String getEditTextValue, String boardId, String contentType, String userId, String dateCreated) {

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));

        restApi.postFeedItemOnBoard(getEditTextValue, boardId, contentType, userId, dateCreated, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {

                        switch (jsonObjectResponse.code()) {
                            case HttpURLConnection.HTTP_OK:
                                //TODO: Refresh content
                                break;
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
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

    }
}
