package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.ZoneAdapter;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class ZoneActivity extends AppCompatActivity {
    private static final String TAG = ZoneActivity.class.getSimpleName();

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    ZoneAdapter zoneAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zone);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initRecyclerView() {
        zoneAdapter = new ZoneAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(zoneAdapter);

    }
}