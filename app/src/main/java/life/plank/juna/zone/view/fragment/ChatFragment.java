package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.LiveZoneActivity;
import life.plank.juna.zone.view.adapter.ChatAdapter;

public class ChatFragment extends Fragment {
    Context context;
    @BindView(R.id.back_image_view)
    TextView backImageView;
    @BindView(R.id.expand_collapse_image_view)
    ImageView expandCollapseImageView;
    @BindView(R.id.people_count_text_view)
    TextView peopleCountTextView;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.camera_image)
    ImageView cameraImage;
    @BindView(R.id.chat_recycler_view)
    RecyclerView chatRecyclerView;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initializeRecyclerView() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ChatAdapter chatAdapter = new ChatAdapter(getActivity());
        chatRecyclerView.setAdapter(chatAdapter);
    }

    //TODO onclick listeners will be added in next pull request
    @OnClick({R.id.back_image_view, R.id.expand_collapse_image_view, R.id.people_count_text_view, R.id.add_image, R.id.camera_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image_view:
                ((LiveZoneActivity)context).isChatScreenVisible = false;
                ((LiveZoneActivity)context).retainLayout();
                break;
            case R.id.expand_collapse_image_view:
                handleExpandCollapse();
                break;
            case R.id.people_count_text_view:
                break;
            case R.id.add_image:
                break;
            case R.id.camera_image:
                break;
        }
    }

    private void handleExpandCollapse(){
        if (expandCollapseImageView.isSelected()) {
            expandCollapseImageView.setSelected(false);
            ((LiveZoneActivity) context).expandCollapseChatView(false);
        }else {
            expandCollapseImageView.setSelected(true);
            ((LiveZoneActivity) context).expandCollapseChatView(true);
        }
    }
}
