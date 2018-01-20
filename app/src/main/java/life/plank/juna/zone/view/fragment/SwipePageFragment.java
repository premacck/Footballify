package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;

/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageFragment extends Fragment implements FootballFeedAdapter.ItemClickListener {

    private int pageNumber;
    FootballFeedAdapter footballFeedAdapter;

    public static SwipePageFragment create(int pageNumber) {
        SwipePageFragment fragment = new SwipePageFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SwipePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_swipe_page, container, false);

        // Set the title view to show the page number.
        //TODO: Will be replaced with the feed grid
        //((TextView) rootView.findViewById(R.id.dummyText)).setText("Hello");

        String[] data = {"1", "2", "3", "4"};

        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.football_feed_recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        footballFeedAdapter = new FootballFeedAdapter(getContext(), data);
        footballFeedAdapter.setClickListener(this);
        recyclerView.setAdapter(footballFeedAdapter);

        return rootView;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public void onItemClick(View view, int position) {



    }
}
