package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import life.plank.juna.zone.R;

/**
 * Created by plank-arfaa on 19/01/18.
 */

public class SwipePageFragment extends Fragment {

    private int pageNumber;

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
        ((TextView) rootView.findViewById(R.id.dummyText)).setText("Hello");

        return rootView;
    }

    public int getPageNumber() {
        return pageNumber;
    }

}
