package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.view.adapter.SearchViewAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class InviteToBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = InviteToBoardActivity.class.getSimpleName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.search_view)
    SearchView search;
    ArrayList<User> userList = new ArrayList<>();
    private SearchViewAdapter adapter;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initRecyclerView();
        search.setQueryHint(getString(R.string.search_query_hint));
    }

    //TODO: Move the card up when the user clicks on the search view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.search_result_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(InviteToBoardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new SearchViewAdapter(userList, this);
        recyclerView.setAdapter(adapter);
        search.setOnQueryTextListener(this);
    }

    private void getSearchedUsers(String displayName) {

        restApi.getSearchedUsers(getToken(this), displayName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<User>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<User>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                adapter.update(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                userList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            getSearchedUsers(s);
        }else {
            userList.clear();
            adapter.notifyDataSetChanged();
        }
        return true;
    }
}
