package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.AppConstants;

public class RecordAudioActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = RecordAudioActivity.class.getSimpleName();
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    @BindView(R.id.start_image_button)
    ImageButton startButton;
    @BindView(R.id.chronometer_text_view)
    Chronometer chronometer;
    @BindView(R.id.audio_image_view)
    ImageView audioImageView;
    @BindView(R.id.time_relative_layout)
    RelativeLayout timeLayout;
    @BindView(R.id.tap_to_hold_text_view)
    TextView tapTextView;
    private boolean permissionToRecordAccepted = false;
    private MediaRecorder recorder = null;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        audioImageView.setBackgroundResource(R.drawable.mic_red);
        startButton.setOnTouchListener(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                timeLayout.setVisibility(View.VISIBLE);
                tapTextView.setVisibility(View.INVISIBLE);
                startRecording();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                return true;
            case MotionEvent.ACTION_UP:
                stopRecording();
                timeLayout.setVisibility(View.INVISIBLE);
                tapTextView.setVisibility(View.VISIBLE);
                chronometer.stop();
                Intent in = new Intent(this, PostRecordedAudioActivity.class);
                in.putExtra(getString(R.string.intent_audio_path), fileName);
                startActivity(in);
                finish();
                return true;
        }
        return false;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        recorder.start();
        audioImageView.setBackgroundResource(R.drawable.mic_red_color);
    }

    private void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            audioImageView.setBackgroundResource(R.drawable.mic_red);
        } catch (Exception e) {
            Log.e(TAG, "release() failed");
        }
    }
}