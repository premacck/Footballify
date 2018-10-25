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
import life.plank.juna.zone.data.model.FeedItemCommentReply;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
        adapter = new PostCommentAdapter(picasso, this);
        postCommentsRecyclerView.setAdapter(adapter);

        FeedItemCommentReply feedItemCommentReply1 = new FeedItemCommentReply("1","Bye","Arfaa","");
        FeedItemCommentReply feedItemCommentReply2 = new FeedItemCommentReply("2","Hi","Snehitha","");
        FeedItemCommentReply feedItemCommentReply3 = new FeedItemCommentReply("3","ByeForever","Dhamini","");

        List<FeedItemCommentReply> feedItemCommentReplies = new ArrayList<>() ;
        feedItemCommentReplies.add(feedItemCommentReply1);
        feedItemCommentReplies.add(feedItemCommentReply2);
        feedItemCommentReplies.add(feedItemCommentReply3);

        FeedItemComment feedItemComment1 = new FeedItemComment("1","xcvzcx","Abczxcxd",
                "zxczx",3,true,3,feedItemCommentReplies);

        FeedItemComment feedItemComment2 = new FeedItemComment("2","tretgdrf","Abcd",
                "zxczxcdrt",3,true,3,feedItemCommentReplies);

        FeedItemComment feedItemComment3 = new FeedItemComment("3","dgerwwe","Abcd",
                "werweds",3,true,3,feedItemCommentReplies);

        List<FeedItemComment> commentList = new ArrayList<>();
        commentList.add(feedItemComment1);
        commentList.add(feedItemComment2);
        commentList.add(feedItemComment3);
        adapter.setComments(commentList);


        return rootView;
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

    }
}
