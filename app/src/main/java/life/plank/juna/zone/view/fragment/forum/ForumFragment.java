package life.plank.juna.zone.view.fragment.forum;

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

import com.squareup.picasso.Picasso;

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
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

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
        getComments();
        return rootView;
    }

    //TODO: Remove hard coded data after backend integration.
    public void setAdapterData() {

        List<FeedItemComment> commentList = new ArrayList<>();
        adapter.setComments(commentList);

    }

    public void getComments() {
        restApi.getCommentsForBoard(matchBoardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<FeedItemComment>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("ForumFragment", "onCompleted : getCommentsForFeed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ForumFragment", e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<FeedItemComment>> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                                adapter.setComments(response.body());
                                break;
                            case HTTP_NOT_FOUND:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_get_feed_comments, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ZoneApplication.getContext(), R.string.failed_to_get_feed_comments, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @OnClick({R.id.post_comment})
    public void onViewClicked() {
        postCommentOnBoard();
    }

    private void postCommentOnBoard() {

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));

        restApi.postCommentOnBoard(commentEditText.getText().toString(), matchBoardId, date, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FeedItemComment>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("ForumFragment", "onCompleted : postComment()");
                    }

                    @Override
                    public void onError(Throwable e) {
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
