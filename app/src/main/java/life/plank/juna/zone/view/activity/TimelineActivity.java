package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.TimelineAdapter;

import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class TimelineActivity extends AppCompatActivity {

    @BindView(R.id.header)
    TextView headerView;
    @BindView(R.id.timeline_recycler_view)
    RecyclerView timelineRecyclerView;

    @Inject
    @Named("footballData")
    RestApi restApi;

    private TimelineAdapter adapter;

    public static void launch(Activity packageContext, View fromView) {
        Intent intent = new Intent(packageContext, TimelineActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(packageContext, Pair.create(fromView, packageContext.getString(R.string.timeline_activity)));
        packageContext.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setupSwipeGesture(this, headerView);

        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), boardParentViewBitmap));

        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new TimelineAdapter(this);
        timelineRecyclerView.setAdapter(adapter);
    }

    private void getTimeLine() {
//        TODO : Write the code to get the match events form API and update adapter call here.
    }
}