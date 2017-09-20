package life.plank.juna.zone.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.dagger.NewsFeedsCustomApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.NewsFeed;
import life.plank.juna.zone.presentation.activity.ZoneHomeActivity;
import life.plank.juna.zone.presentation.adapter.NewsFeedsAdapter;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsFeedsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    Retrofit retrofit;

    private static final String TAG = ZoneHomeActivity.class.getSimpleName();
    private Subscription subscription;
    private RestApi restApi;
    private NewsFeedsAdapter newsFeedsAdapter = new NewsFeedsAdapter();
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feeds, container, false);
        ButterKnife.bind(this, view);
        date = new SimpleDateFormat(getActivity().getString(R.string.date)).format(new Date());
        initRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((NewsFeedsCustomApplication) getActivity().getApplication()).getNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        loadNewsFeeds(date);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newsFeedsAdapter);

        subscription = newsFeedsAdapter.getViewClickedObservable()
                .subscribe(new Observer<NewsFeed>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted() adapter");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError() adapter:" + e.getMessage());
                    }

                    @Override
                    public void onNext(NewsFeed newsFeed) {
                        Toast.makeText(getActivity(), " Feed clicked: " + newsFeed.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadNewsFeeds(String date) {
        subscription = restApi.getNewsFeed(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewsFeed>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError():" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<NewsFeed> newsFeeds) {
                        newsFeedsAdapter.setNewsFeedList(newsFeeds);
                    }
                });
    }
}