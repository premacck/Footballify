package life.plank.juna.zone.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.MediaSelectionFragmentActionInterface;
import life.plank.juna.zone.interfaces.ScrollRecyclerView;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.AudioRecorderActivity;
import life.plank.juna.zone.view.activity.LiveZoneActivity;
import life.plank.juna.zone.view.adapter.ChatAdapter;
import life.plank.juna.zone.viewmodel.ChatModel;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static life.plank.juna.zone.util.AppConstants.CAMERA_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.RECORDED_AUDIO;
import static life.plank.juna.zone.util.AppConstants.REQUEST_AUDIO_RECORDER;
import static life.plank.juna.zone.util.AppConstants.REQUEST_CAMERA_STORAGE;
import static life.plank.juna.zone.util.AppConstants.REQUEST_GALLERY;
import static life.plank.juna.zone.util.AppConstants.REQUEST_VIDEO_CAPTURE;

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
    @BindView(R.id.video_recorder)
    ImageView videoRecorder;
    @BindView(R.id.audio_recorder)
    ImageView audioRecorder;
    ChatAdapter chatAdapter;
    String recordedVideoUrl;
    private Uri imageUri;
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

    @OnClick({R.id.back_image_view, R.id.expand_collapse_image_view, R.id.people_count_text_view, R.id.add_image, R.id.camera_image, R.id.send_text_view, R.id.video_recorder, R.id.audio_recorder})
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
            case R.id.video_recorder:
                dispatchTakeVideoIntent();
                break;
            case R.id.audio_recorder:
                dispatchRecordAudioIntent();
            default:
                break;
        }
    }

    private void dispatchRecordAudioIntent() {
        Intent intent = new Intent(getApplicationContext(),AudioRecorderActivity.class);
        startActivityForResult(intent,REQUEST_AUDIO_RECORDER);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
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
        try {
            File file = createImageFile();
            imageUri = FileProvider.getUriForFile(context, AppConstants.FILE_PROVIDER_TO_CAPTURE_IMAGE, file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_IMAGE_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable commentText) {
                if (commentText.length() > 0) {
                    sendTextView.setVisibility(View.VISIBLE);
                } else {
                    sendTextView.setVisibility(View.GONE);
                }
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE_RESULT) {
            if (resultCode == RESULT_OK) {
                if (imageUri != null) {
                    sendImage(imageUri.getPath().replace("//", "/"));
                }
            } else {
                Toast.makeText(context, context.getString(R.string.unable_to_capture_image), Toast.LENGTH_SHORT).show();
                if (imageUri != null) {
                    new File(imageUri.getPath().replace("//", "/")).delete();
                }
            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            recordedVideoUrl = String.valueOf(data.getData());
            sendVideoToChat(recordedVideoUrl);
        }
        if (requestCode == REQUEST_AUDIO_RECORDER && resultCode == RESULT_OK) {
            String recordedAudio =  data.getExtras().getString(RECORDED_AUDIO);
            sendAudioToChat(recordedAudio);
        }
    }

    private void sendVideoToChat(String recordedVideoUrl) {
        chatAdapter.addMessage(new ChatModel("image", context.getString(R.string.lorem_ipsum_text), Uri.fromFile(new File(recordedVideoUrl)).toString(), true));
    }

    private void sendAudioToChat(String recordedAudio) {
        chatAdapter.addMessage(new ChatModel("image", context.getString(R.string.lorem_ipsum_text), AppConstants.DEFAULT_RECORDED_AUDIO_IMAGE, true));
    }

    private void sendImage(String imageUri) {
        chatAdapter.addMessage(new ChatModel("image", context.getString(R.string.lorem_ipsum_text), Uri.fromFile(new File(imageUri)).toString(), true));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date());
        String imageFileName = AppConstants.CAPTURED_IMAGE_NAME + timeStamp + AppConstants.CAPTURED_IMAGE_FORMAT;
        File mediaStorageDirectory = new File(Environment.getExternalStorageDirectory(),
                AppConstants.CAPTURED_IMAGES_FOLDER_NAME);
        File storageDirectory = new File(mediaStorageDirectory + File.separator + AppConstants.CAPTURED_IMAGES_SUB_FOLDER_NAME);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }
        return new File(storageDirectory, imageFileName);
    }
}