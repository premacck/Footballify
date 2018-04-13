package life.plank.juna.zone.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.MediaSelectionFragmentActionInterface;
import life.plank.juna.zone.interfaces.ScrollRecyclerView;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.LiveZoneActivity;
import life.plank.juna.zone.view.adapter.ChatAdapter;
import life.plank.juna.zone.viewmodel.ChatModel;

import static life.plank.juna.zone.util.AppConstants.REQUEST_CAMERA_STORAGE;
import static life.plank.juna.zone.util.AppConstants.REQUEST_GALLERY;

public class ChatFragment extends Fragment implements MediaSelectionFragmentActionInterface, ScrollRecyclerView {
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
    @BindView(R.id.media_container_frame_layout)
    FrameLayout mediaContainerFrameLayout;
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    @BindView(R.id.send_text_view)
    TextView sendTextView;
    ChatAdapter chatAdapter;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        setUpMessageEditTextChangeListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initializeRecyclerView() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        chatAdapter = new ChatAdapter(getActivity(), this);
        chatRecyclerView.setAdapter(chatAdapter);
        scrollRecyclerViewToBottom();
    }

    @OnClick({R.id.back_image_view, R.id.expand_collapse_image_view, R.id.people_count_text_view, R.id.add_image, R.id.camera_image, R.id.send_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image_view:
                UIDisplayUtil.getInstance().hideSoftKeyboard(getView(), getActivity());
                ((LiveZoneActivity) context).isChatScreenVisible = false;
                ((LiveZoneActivity) context).retainLayout();
                break;
            case R.id.expand_collapse_image_view:
                handleExpandCollapseView();
                break;
            case R.id.people_count_text_view:
                break;
            case R.id.add_image:
                if (isStoragePermissionGranted(REQUEST_GALLERY, false)) {
                    mediaSelectionFragment();
                    addImage.setVisibility(View.GONE);
                }
                break;
            case R.id.camera_image:
                if (isStoragePermissionGranted(REQUEST_CAMERA_STORAGE, true))
                    takePicture();
                break;
            case R.id.send_text_view:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void handleExpandCollapseView() {
        if (expandCollapseImageView.isSelected()) {
            expandCollapseImageView.setImageResource(R.drawable.ic_expand);
            expandCollapseImageView.setSelected(false);
            ((LiveZoneActivity) context).expandCollapseChatView(false);
        } else {
            expandCollapseImageView.setImageResource(R.drawable.ic_collapse);
            expandCollapseImageView.setSelected(true);
            ((LiveZoneActivity) context).expandCollapseChatView(true);
        }
    }

    private void mediaSelectionFragment() {
        mediaContainerFrameLayout.removeAllViews();
        MediaSelectionFragment mediaSelectionFragment = new MediaSelectionFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.media_container_frame_layout, mediaSelectionFragment)
                .commit();
        mediaSelectionFragment.setMediaSelectionInterface(this);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mediaSelectionFragment();
                break;
            case REQUEST_CAMERA_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            default:
                break;
        }
    }

    private boolean isStoragePermissionGranted(int requestCode, boolean isCamera) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isCamera) {
                if (context.checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
                    return false;
                }
            } else {
                if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    @Override
    public void closeMediaFragment() {
        addImage.setVisibility(View.VISIBLE);
        mediaContainerFrameLayout.removeAllViews();
    }

    private void setUpMessageEditTextChangeListener() {
        commentEditText.addTextChangedListener(new TextWatcher() {
                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                 int after) {
                                                   }

                                                   @Override
                                                   public void onTextChanged(final CharSequence s, int start, int before,
                                                                             int count) {
                                                   }

                                                   @Override
                                                   public void afterTextChanged(final Editable commentText) {
                                                       //avoid triggering event when text is empty
                                                       if (commentText.length() > 0) {
                                                           sendTextView.setVisibility(View.VISIBLE);
                                                       } else {
                                                           sendTextView.setVisibility(View.GONE);
                                                       }
                                                   }
                                               }
        );
    }

    private void sendMessage() {
        chatAdapter.addMessage(new ChatModel("text", commentEditText.getText().toString(), "", true));
        commentEditText.setText("");
        sendTextView.setVisibility(View.GONE);
    }


    @Override
    public void scrollRecyclerViewToBottom() {
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }
}