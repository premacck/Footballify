package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class WorldCupFixtureActivity extends AppCompatActivity {
    String foreignId1 = "1001";
    String foreignId2 = "1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_cup_fixture);
        ButterKnife.bind(this);

        ImageView image1 = findViewById(R.id.uruguay_flag);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorldCupFixtureActivity.this, BoardActivity.class);
                intent.putExtra(getString(R.string.intent_foreign_id_1), foreignId1);
                startActivity(intent);
            }
        });

        ImageView image2 = findViewById(R.id.brazil_flag);
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorldCupFixtureActivity.this, BoardActivity.class);
                intent.putExtra(getString(R.string.intent_foreign_id_2), foreignId2);
                startActivity(intent);
            }
        });

    }

}
