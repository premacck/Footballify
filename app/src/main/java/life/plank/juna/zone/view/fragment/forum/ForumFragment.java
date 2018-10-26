package life.plank.juna.zone.view.fragment.forum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;

import static life.plank.juna.zone.ZoneApplication.getApplication;

public class ForumFragment extends Fragment implements FeedInteractionListener {

    @Inject
    Picasso picasso;
    @BindView(R.id.post_comments_list)
    RecyclerView postCommentsRecyclerView;
    private PostCommentAdapter adapter;

    public static ForumFragment newInstance() {
        ForumFragment fragment = new ForumFragment();
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
