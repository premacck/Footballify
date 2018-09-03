package life.plank.juna.zone.view.fragment.board.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;

public class PrivateBoardInfoFragment extends Fragment {

    private static final String TAG = PrivateBoardInfoFragment.class.getSimpleName();
    private static final String DESCRIPTION = "description";

    @Inject
    @Named("default")
    RestApi restApi;
    @BindView(R.id.description)
    TextView descriptionTextView;
    private String description;

    public PrivateBoardInfoFragment() {
    }

    public static PrivateBoardInfoFragment newInstance(String description) {
        PrivateBoardInfoFragment fragment = new PrivateBoardInfoFragment();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            description = args.getString(DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_private_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        descriptionTextView.setText(description);
        return rootView;
    }
}