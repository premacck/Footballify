package life.plank.juna.zone.presentation.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.CustomizeStatusBar;

public class CreateArenaActivity extends AppCompatActivity {

    @BindView(R.id.text_secret_code)
    TextView secretCodeText;
    @BindView(R.id.text_share_secret_code)
    TextView shareSecretCodeText;
    @BindView(R.id.user_list)
    ListView userListView;
    @BindView(R.id.secret_code)
    TextView secretCode;

    //Todo: Remove dummy data once usernames are retrieved from database on arena creation
    String[] usersForArena = {
            "Safari",
            "Global",
            "FireFox",
            "UC Browser",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_arena);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        Typeface moderneSansFont = Typeface.createFromAsset(getAssets(), "font/moderne_sans.ttf");
        secretCodeText.setTypeface(moderneSansFont);
        secretCode.setTypeface(moderneSansFont);
        Typeface arsenalFont = Typeface.createFromAsset(getAssets(), "font/arsenal_regular.otf");
        shareSecretCodeText.setTypeface(arsenalFont);

        userListView.setAdapter(new ArrayAdapter<>(
                this, R.layout.user_list_view_row,
                R.id.user_name, usersForArena));
    }
}
