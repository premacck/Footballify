package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.helper.ItemTouchHelperCallback;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

/**
 * Created by plank-niraj on 26-01-2018.
 */
public class ScrubberActivity extends AppCompatActivity {

    final ArrayList<String> data = new ArrayList<>();
    @BindView(R.id.scrubber_recycler_view)
    RecyclerView scrubberRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrubber);
        ButterKnife.bind(this);
    }

}

