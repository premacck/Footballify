package life.plank.juna.zone.view.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.AppConstants;

public class PostRecordedAudioActivity extends AppCompatActivity {
    private static final String LOG_TAG = PostRecordedAudioActivity.class.getSimpleName();
    String audioFile;
    @BindView(R.id.play_button)
    ImageView playImageView;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_recorded_audio);
        ButterKnife.bind(this);
        audioFile = getIntent().getStringExtra(getString(R.string.intent_audio_path));
    }

    @OnClick({R.id.play_button})
    public void onViewClicked(View view) {
        boolean startPlaying = true;
        switch (view.getId()) {
            case R.id.play_button:
                if (view.isSelected()) {
                    stopPlaying();
                } else {
                    view.setSelected(true);
                    onPlay(startPlaying);
                }
                break;
        }
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
