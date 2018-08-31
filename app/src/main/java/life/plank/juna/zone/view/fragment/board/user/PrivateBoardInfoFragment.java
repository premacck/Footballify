package life.plank.juna.zone.view.fragment.board.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;

public class PrivateBoardInfoFragment extends Fragment {

    private static final String TAG = PrivateBoardInfoFragment.class.getSimpleName();

    @Inject
    @Named("default")
    RestApi restApi;

    public PrivateBoardInfoFragment() {
    }

    public static PrivateBoardInfoFragment newInstance() {
        PrivateBoardInfoFragment fragment = new PrivateBoardInfoFragment();
        Bundle args = new Bundle();
//        TODO : pass arguments here
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TODO : check arguments and retrieve them from bundle
//        Bundle args = getArguments();
//        if (args != null) {
//            mParam1 = args.getString(ARG_PARAM1);
//            mParam2 = args.getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_private_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        return rootView;
    }
}