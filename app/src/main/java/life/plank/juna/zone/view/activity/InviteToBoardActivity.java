package life.plank.juna.zone.view.activity;

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
    ArrayList<Integer> profileImageList = new ArrayList<>();
    ArrayList<String> usernameList = new ArrayList<>();
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
        for (int i = 0; i <= 4; i++) {
            profileImageList.add(R.drawable.ic_profile_dummy);
        }
        usernameList.add("Anna Jule");
        usernameList.add("Crisp Rat");
        usernameList.add("Jon Doe");
        usernameList.add("Wing Moe");
        usernameList.add("John Dexter");
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.search_result_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(InviteToBoardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new SearchViewAdapter(profileImageList, usernameList);
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
