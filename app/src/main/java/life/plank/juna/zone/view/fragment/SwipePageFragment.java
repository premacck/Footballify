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

public class SwipePageFragment extends Fragment {

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

        //TODO: Will be replaced with data from backend
        String[] data = {"Reece Burke celebrates after breaking the deadlock in extra time at London Stadium",
                "Utd to subsidised fans in Sevilla row. $89 to visit Old Trafford for spanish supporters",
                "Charlie Nicholas : FA Cup third round replay predictions",
                "Brighton vs Chelsea"};

        // set up the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.football_feed_recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        footballFeedAdapter = new FootballFeedAdapter(getContext(), data);
        recyclerView.setAdapter(footballFeedAdapter);

        return rootView;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
