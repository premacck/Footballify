package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.view.adapter.BoardColorThemeAdapter;
import life.plank.juna.zone.view.adapter.BoardIconAdapter;

import static com.facebook.internal.Utility.isNullOrEmpty;
import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;
import static life.plank.juna.zone.util.UIDisplayUtil.enableOrDisableView;
import static life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.toggleZone;

public class CreateBoardActivity extends AppCompatActivity {

    public static Bitmap parentViewBitmap = null;

    @BindView(R.id.parent_layout)
    ScrollView parentLayout;
    @BindView(R.id.football)
    ToggleButton football;
    @BindView(R.id.music)
    ToggleButton music;
    @BindView(R.id.drama)
    ToggleButton drama;
    @BindView(R.id.tune)
    ToggleButton tune;
    @BindView(R.id.skill)
    ToggleButton skill;
    @BindView(R.id.other)
    ToggleButton other;
    @BindView(R.id.board_name_edit_text)
    TextInputEditText boardName;
    @BindView(R.id.board_description_edit_text)
    TextInputEditText boardDescription;
    @BindView(R.id.private_board_color_list)
    RecyclerView privateBoardColorList;
    @BindView(R.id.private_board_icon_list)
    RecyclerView privateBoardIconList;
    @BindView(R.id.upload_board_icon)
    Button uploadBoardIcon;
    @BindView(R.id.create_board_button)
    Button createPrivateBoard;
    @BindView(R.id.tool_bar)
    ZoneToolBar toolbar;
    @BindView(R.id.user_greeting)
    TextView userGreeting;

    @Inject
    BoardColorThemeAdapter boardColorThemeAdapter;
    @Inject
    BoardIconAdapter boardIconAdapter;

    private ToggleButton[] zones;
    private String zone;
    private String filePath;
    private Boolean isIconSelected = false;

    public static void launch(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, CreateBoardActivity.class);
        intent.putExtra(ZoneApplication.getContext().getString(R.string.username), username);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        zones = new ToggleButton[]{football, music, drama, tune, skill, other};
        privateBoardColorList.setAdapter(boardColorThemeAdapter);
        privateBoardIconList.setAdapter(boardIconAdapter);

        UIDisplayUtil.checkPermission(CreateBoardActivity.this);
        boardIconAdapter.boardIconList.clear();

        validateCreateBoardContent();

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        if (!sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)).equals(getString(R.string.na))) {
            toolbar.setProfilePic(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)));
        }
        userGreeting.setText(getString(R.string.hi_user, getIntent().getStringExtra(ZoneApplication.getContext().getString(R.string.username))));
    }

    @OnTextChanged({R.id.board_name_edit_text, R.id.board_description_edit_text})
    public void onTextFieldUpdated() {
        validateCreateBoardContent();
    }

    @OnClick({R.id.football, R.id.music, R.id.drama, R.id.tune, R.id.skill, R.id.other})
    public void toggleView(ToggleButton view) {
        for (ToggleButton zoneView : zones) {
            if (zoneView.getId() == view.getId()) {
                checkAction(zoneView, !zoneView.isChecked());
                continue;
            }
            checkAction(zoneView, false);
        }
        validateCreateBoardContent();
    }

    private void checkAction(ToggleButton toggleButton, boolean isChecked) {
        if (Objects.equals(zone, toggleButton.getText().toString())) {
            if (!isChecked) {
                zone = null;
            }
        } else {
            if (isChecked) {
                zone = toggleButton.getText().toString();
            }
        }
        toggleZone(this, toggleButton, isChecked);
    }

    @OnClick(R.id.create_board_button)
    public void onViewClicked(View view) {
        Board board = new Board(
                boardName.getText().toString().trim(),
                getString(R.string.private_lowercase),
                zone.toLowerCase().trim(),
                boardDescription.getText().toString().trim(),
                boardColorThemeAdapter.getSelectedColor()
        );

        createBoard(board, boardIconAdapter.getSelectedPath());
    }

    @OnClick(R.id.upload_board_icon)
    public void onButtonClicked(View view) {
        if (UIDisplayUtil.checkPermission(CreateBoardActivity.this)) {
            getImageResourceFromGallery();
        } else {
            Toast.makeText(this, R.string.add_permission, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GALLERY_IMAGE_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        filePath = getPathForGalleryImageView(data.getData(), this);
                        if (filePath != null) {
                            boardIconAdapter.boardIconList.add(0, filePath);
                            boardIconAdapter.setSelectedIndex(0);
                            boardIconAdapter.notifyItemInserted(0);
                            isIconSelected = true;
                            validateCreateBoardContent();
                        } else {
                            Toast.makeText(this, R.string.image_not_supported, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        Toast.makeText(this, R.string.failed_to_process_image, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
                break;
            default:
                break;
        }
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format));
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }

    private void validateCreateBoardContent() {
        enableOrDisableView(
                createPrivateBoard,
                !(isNullOrEmpty(zone) || isNullOrEmpty(zone.toLowerCase().trim()) ||
                        boardName.getText() == null || isNullOrEmpty(boardName.getText().toString().trim()) ||
                        boardDescription.getText() == null || isNullOrEmpty(boardDescription.getText().toString().trim()) ||
                        isNullOrEmpty(boardColorThemeAdapter.getSelectedColor()) || !isIconSelected)
        );
    }

    private void createBoard(Board board, String file) {
        if (isNullOrEmpty(file)) {
            Toast.makeText(this, R.string.select_image_to_upload, Toast.LENGTH_SHORT).show();
            return;
        }

        parentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
        BoardPreviewActivity.launch(this, board, file);
    }
}