package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

/**
 * Created by plank-dhamini on 21/12/17.
 */

public class WarriorWinnerActivity extends AppCompatActivity {

    @BindView(R.id.score_text)
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior_winner);

        ButterKnife.bind(this);
        score.setText(getIntent().getStringExtra("score"));
    }

    @OnClick(R.id.home_icon)
    public void onClick() {
        Intent intent = new Intent(this, ClubListViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
