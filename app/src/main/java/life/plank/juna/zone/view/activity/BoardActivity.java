package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity {

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        LiveZoneGridAdapter footballFeedAdapter = new LiveZoneGridAdapter(this);
        boardRecyclerView.setAdapter(footballFeedAdapter);
        boardRecyclerView.setHasFixedSize(true);
        /*footballFeedAdapter.setPinFeedListener(this);
        footballFeedAdapter.setOnLongPressListener(this);*/
    }

}
