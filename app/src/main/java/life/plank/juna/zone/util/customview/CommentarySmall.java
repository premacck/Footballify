package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.view.adapter.CommentaryAdapter;

public class CommentarySmall extends FrameLayout implements CustomViewListener {

    @BindView(R.id.see_all)
    Button seeAllBtn;
    @BindView(R.id.commentary_list)
    RecyclerView commentaryRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noDataTextView;

    private CommentarySmallListener listener;
    private CommentaryAdapter adapter;

    public CommentarySmall(@NonNull Context context) {
        this(context, null);
    }

    public CommentarySmall(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentarySmall(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CommentarySmall(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_live_commentary_small, this);
        ButterKnife.bind(this, rootView);

        ((LinearLayoutManager) commentaryRecyclerView.getLayoutManager()).setReverseLayout(true);
        seeAllBtn.setOnClickListener(view -> listener.seeAllClicked());
    }

    public void setAdapter(CommentaryAdapter adapter) {
        this.adapter = adapter;
        commentaryRecyclerView.setAdapter(adapter);
    }

    public void updateAdapter(List<Commentary> commentaries) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        commentaryRecyclerView.setVisibility(VISIBLE);
        adapter.update(commentaries);
        commentaryRecyclerView.scrollToPosition(commentaries.size() - 1);
    }

    public void updateNew(List<Commentary> commentaries) {
        adapter.updateNew(commentaries);
        commentaryRecyclerView.smoothScrollToPosition(adapter.getCommentaries().size() + commentaries.size() - 1);
    }

    public void notAvailable(@StringRes int message) {
        noDataTextView.setText(message);
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(VISIBLE);
        commentaryRecyclerView.setVisibility(INVISIBLE);
    }

    public View getRootLayout() {
        return this;
    }

    public List<Commentary> getCommentaryList() {
        return adapter.getCommentaries();
    }

    @Override
    public void initListeners(Fragment fragment) {
        if (fragment instanceof CommentarySmallListener) {
            listener = (CommentarySmallListener) fragment;
        } else throw new IllegalStateException("Fragment must implement CommentarySmallListener");
    }

    @Override
    public void initListeners(Activity activity) {
        if (activity instanceof CommentarySmallListener) {
            listener = (CommentarySmallListener) activity;
        } else throw new IllegalStateException("Activity must implement CommentarySmallListener");
    }

    public void dispose() {
        listener = null;
    }

    public interface CommentarySmallListener {
        void seeAllClicked();
    }
}