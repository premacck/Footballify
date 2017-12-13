package life.plank.juna.zone.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class SuddenDeathWinnerOrLoserActivity extends AppCompatActivity {

    @BindView(R.id.text_message_youre)
    TextView messageYoureLabel;
    @BindView(R.id.text_message)
    TextView messageView;
    @BindView(R.id.football_image)
    ImageView footballImage;

    private String selectedTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudden_death_winner_loser);
        ButterKnife.bind(this);
        CustomizeStatusBar.removeStatusBar(getWindow());
        String result = getIntent().getStringExtra(getString(R.string.result_string));
        messageView.setText(result);
        selectedTeamName = getIntent().getStringExtra(getString(R.string.selected_team));

        Typeface lemonMilkFont = Typeface.createFromAsset(getAssets(), getString(R.string.lemon_milk));
        messageYoureLabel.setTypeface(lemonMilkFont);
        messageView.setTypeface(lemonMilkFont);

        rotateFootballImage();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, SuddenDeathResultActivity.class);
            intent.putExtra(getString(R.string.selected_team), selectedTeamName);
            startActivity(intent);
        }, getResources().getInteger(R.integer.SPLASH_TIME_OUT));
    }

    private void rotateFootballImage() {
        //Todo: Replace the src image for footballImage view with a 3D football image
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(footballImage, getString(R.string.property_type), 0f, 360f);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();
    }

    @Override
    public void onBackPressed() {

    }
}
