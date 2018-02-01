package life.plank.juna.zone.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.FootballFeedDetailAdapter;

public class FootballFeedDetailActivity extends AppCompatActivity {

    @BindView(R.id.football_feed_recycler_view)
    RecyclerView footballFeedRecyclerView;
    @BindView(R.id.image_cancel)
    ImageView cancelImageView;
    private FootballFeedDetailAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_feed_detail);
        ButterKnife.bind(this);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String[] playersDetails = {"TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE", "TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE"};
        mAdapter = new FootballFeedDetailAdapter(this, playersDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        footballFeedRecyclerView.setLayoutManager(mLayoutManager);
        footballFeedRecyclerView.setAdapter(mAdapter);


    }
}
