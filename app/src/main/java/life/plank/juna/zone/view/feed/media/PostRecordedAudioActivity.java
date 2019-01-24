package life.plank.juna.zone.view.feed.media;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import life.plank.juna.zone.R;

public class PostRecordedAudioActivity extends AppCompatActivity {
    private static final String LOG_TAG = PostRecordedAudioActivity.class.getSimpleName();
    String audioFile;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recorded_audio);
        audioFile = getIntent().getStringExtra(getString(R.string.intent_audio_path));

        findViewById(R.id.play_button).setOnClickListener(view -> {
            boolean startPlaying = true;
            if (view.isSelected()) {
                stopPlaying();
            } else {
                view.setSelected(true);
                onPlay(startPlaying);
            }
        });
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audioFile);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
