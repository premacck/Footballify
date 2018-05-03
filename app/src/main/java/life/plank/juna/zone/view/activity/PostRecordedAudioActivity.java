package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import life.plank.juna.zone.R;

public class PostRecordedAudioActivity extends AppCompatActivity {
    String audioFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recorded_audio);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            audioFile = null;
        } else {
            audioFile = extras.getString("audiopath");
        }
    }
}
