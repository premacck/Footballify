package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.view.adapter.CommentaryAdapter;

public class CommentarySmall extends FrameLayout implements CustomViewListener {

    private RecyclerView commentaryList;
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

    private void initVariables() {
        Button seeAllBtn = findViewById(R.id.see_all);
        commentaryList = findViewById(R.id.commentary_list);

        seeAllBtn.setOnClickListener(view -> listener.seeAllClicked());
    }

    private void init(Context context) {
        inflate(context, R.layout.item_live_commentary_small, this);
        initVariables();
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

    private void setAdapter(CommentaryAdapter adapter) {
        this.adapter = adapter;
        commentaryList.setAdapter(adapter);
    }

    private void notifyAdapter(List<Commentary> commentaries) {
        adapter.update(commentaries);
    }

    public interface CommentarySmallListener {
        void seeAllClicked();
    }
}