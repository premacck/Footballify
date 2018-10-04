package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Commentary;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.CommentaryAdapter;

import static life.plank.juna.zone.util.AppConstants.COMMENTARY_DATA;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.base.BaseBoardActivity.boardParentViewBitmap;

public class CommentaryActivity extends AppCompatActivity {

    @BindView(R.id.header)
    TextView headerView;
    @BindView(R.id.commentary_recycler_view)
    RecyclerView commentaryRecyclerView;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Gson gson;

    private List<Commentary> commentaryList;
    private CommentaryAdapter adapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                updateLiveCommentary(getZoneLiveData(intent, context.getString(R.string.intent_zone_live_data), gson));
            }
        }
    };

    public static void launch(Activity activity, View fromView, List<Commentary> commentaryList) {
        Intent intent = new Intent(activity, CommentaryActivity.class);
        intent.putParcelableArrayListExtra(activity.getString(R.string.commentary), (ArrayList<? extends Parcelable>) commentaryList);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, Pair.create(fromView, activity.getString(R.string.commentary)));
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);
        ButterKnife.bind(this);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerView);

        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), boardParentViewBitmap));

        Intent intent = getIntent();
        if (intent != null) {
            commentaryList = intent.getParcelableArrayListExtra(getString(R.string.commentary));
        }
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.intent_board)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    private void initRecyclerView() {
        if (!isNullOrEmpty(commentaryList)) {
            adapter = new CommentaryAdapter(commentaryList);
            ((LinearLayoutManager) commentaryRecyclerView.getLayoutManager()).setReverseLayout(true);
            commentaryRecyclerView.setAdapter(adapter);
            commentaryRecyclerView.scrollToPosition(commentaryList.size() - 1);
        }
    }

    private void updateLiveCommentary(ZoneLiveData zoneLiveData) {
        if (zoneLiveData.getLiveDataType().equals(COMMENTARY_DATA)) {
            List<Commentary> commentaryList = zoneLiveData.getCommentaryList(gson);
            if (!isNullOrEmpty(commentaryList)) {
                adapter.updateNew(commentaryList);
                commentaryRecyclerView.smoothScrollToPosition(adapter.getCommentaries().size() + commentaryList.size() - 1);
            }
        }
    }
}