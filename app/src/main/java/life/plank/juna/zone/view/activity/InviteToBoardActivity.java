package life.plank.juna.zone.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.SearchViewAdapter;

public class InviteToBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.search_view)
    SearchView search;
    ArrayList<Integer> viewColors = new ArrayList<>();
    ArrayList<String> animalNames = new ArrayList<>();
    private SearchViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_board);
        ButterKnife.bind(this);
        setArrayListData();
        initRecyclerView();
    }

    private void setArrayListData() {
        //TODO: To be replaced with data from backend.

        viewColors.add(Color.BLUE);
        viewColors.add(Color.YELLOW);
        viewColors.add(Color.MAGENTA);
        viewColors.add(Color.RED);
        viewColors.add(Color.BLACK);

        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.search_result_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(InviteToBoardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new SearchViewAdapter(this, viewColors, animalNames);
        recyclerView.setAdapter(adapter);
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        adapter.filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filter(s);
        return true;
    }
}
