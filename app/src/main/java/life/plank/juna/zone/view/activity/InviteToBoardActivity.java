package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

import life.plank.juna.zone.R;

public class InviteToBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_board);

        SearchView search = findViewById(R.id.search_view);
        search.setQueryHint("Search and add friends");

    }
}
