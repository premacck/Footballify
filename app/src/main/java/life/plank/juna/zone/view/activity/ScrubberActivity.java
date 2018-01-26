package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

    @BindView(R.id.scrubber_recycler_view)
    RecyclerView scrubberRecyclerView;


    ScrubberViewAdapter adapter;
    final ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrubber);
        ButterKnife.bind(this);
        scrubberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new ScrubberViewAdapter(this, data);
        scrubberRecyclerView.setAdapter(adapter);
        setUpScrubber();

    }

    private void setUpScrubber() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scrubberRecyclerView);
        data.add("cursor");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progressStatus = 0;
                // TODO: 26-01-2018 decide based on device width
                while (progressStatus < 70) {
                    // Update the progress status
                    progressStatus += 1;
                    // Try to sleep the thread for 20 milliseconds
                    //Change according to server request
                    try {
                        data.remove(data.size() - 1);
                        if (progressStatus == 30)
                            data.add("half");
                        else if (progressStatus == 10 || progressStatus == 40 || progressStatus == 50)
                            data.add("goal");
                        else
                            data.add("normal");
                        data.add("cursor");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        }).start(); // Start the operation
    }
}

