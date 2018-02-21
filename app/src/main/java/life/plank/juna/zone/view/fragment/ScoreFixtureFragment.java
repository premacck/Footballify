package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.adapter.ScoreFixtureAdapter;


public class ScoreFixtureFragment extends Fragment {
    Context context;
    @BindView(R.id.fixture_recyclerview)
    RecyclerView fixtureRecyclerView;
    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    public ScoreFixtureFragment() {
    }

    //TODO: Rename and change types and number of parameters
    public static ScoreFixtureFragment newInstance(String param1, String param2) {
        ScoreFixtureFragment fragment = new ScoreFixtureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_fixture, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        return view;
    }
    private void initializeRecyclerView() {
        fixtureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        ScoreFixtureAdapter fixtureAdapter = new ScoreFixtureAdapter(getActivity());
        fixtureRecyclerView.setAdapter(fixtureAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
