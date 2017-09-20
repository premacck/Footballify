package life.plank.juna.zone.presentation.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.presentation.activity.ZoneHomeActivity;
import life.plank.juna.zone.presentation.adapter.NewsFeedsAdapter;
import life.plank.juna.zone.presentation.model.NewsFeed;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class NewsFeedsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final String TAG = ZoneHomeActivity.class.getSimpleName();
    private Subscription subscription;
    private List<NewsFeed> newsFeedList = new ArrayList<>();
    private NewsFeedsAdapter newsFeedsAdapter = new NewsFeedsAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feeds, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newsFeedsAdapter);
        prepareData();
        creatingObservable();
        return view;
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

    private void creatingObservable() {
        subscription = Observable.just(newsFeedList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewsFeed>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError():" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<NewsFeed> newsFeed) {
                        Log.d(TAG, "In onNext()");
                        newsFeedsAdapter.setNewsFeedList(newsFeed);
                    }
                });
    }

    private void prepareData() {
        NewsFeed newsFeed = new NewsFeed(R.drawable.image5, "Thierry Henry: Finishing at speed");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image3, "Score Freekicks like Cristiano Ronaldo");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image4, "David Degea plays for ManUnited as a goalkeeper");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image2, "Score Freekicks like Cristiano Ronaldo");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image5, "Thierry Henry: Finishing at speed");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image1, "David Degea plays for ManUnited as a goalkeeper");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image3, "Score Freekicks like Cristiano Ronaldo");
        newsFeedList.add(newsFeed);

        newsFeed = new NewsFeed(R.drawable.image5, "Thierry Henry: Finishing at speed");
        newsFeedList.add(newsFeed);
    }
}
