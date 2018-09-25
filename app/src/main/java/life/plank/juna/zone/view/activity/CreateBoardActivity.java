package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.BoardColorThemeAdapter;
import life.plank.juna.zone.view.adapter.BoardIconAdapter;
import retrofit2.Retrofit;

import static com.facebook.internal.Utility.isNullOrEmpty;
import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;
import static life.plank.juna.zone.util.DataUtil.getMediaType;
import static life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.toggleZone;

public class CreateBoardActivity extends AppCompatActivity {

    private static final String TAG = CreateBoardActivity.class.getSimpleName();
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
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    @Inject
    BoardColorThemeAdapter boardColorThemeAdapter;
    @Inject
    BoardIconAdapter boardIconAdapter;
    @Inject
    Gson gson;
    @BindView(R.id.toggleGroup)
    RadioGroup toggleGroup;
    private RestApi restApi;
    private String zone = "";
    private String filePath;
    private Boolean isColorSelected;
    private Boolean isIconSelected = false;
    private Boolean isZoneSelected = false;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            validateFields();
        }
    };

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, CreateBoardActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        restApi = retrofit.create(RestApi.class);
        privateBoardColorList.setAdapter(boardColorThemeAdapter);
        privateBoardIconList.setAdapter(boardIconAdapter);

        UIDisplayUtil.checkPermission(CreateBoardActivity.this);
        boardIconAdapter.boardIconList.clear();

        boardName.addTextChangedListener(textWatcher);
        boardDescription.addTextChangedListener(textWatcher);

        validateFields();
    }

    @OnClick({R.id.football, R.id.music, R.id.drama, R.id.tune, R.id.skill, R.id.other})
    public void toggleView(ToggleButton view) {
        toggleZone(this, view);
        zone = view.getText().toString();
        isZoneSelected = !view.isChecked();
        validateFields();
    }

    @OnClick(R.id.create_board_button)
    public void onViewClicked(View view) {
        createBoard(new Board(
                boardName.getText().toString().trim(),
                getString(R.string.private_lowercase),
                zone.toLowerCase().trim(),
                boardDescription.getText().toString().trim(),
                boardColorThemeAdapter.getSelectedColor()
        ), boardIconAdapter.getSelectedPath());
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
                        if (getMediaType(filePath) != null) {
                            boardIconAdapter.boardIconList.add(0, filePath);
                            boardIconAdapter.notifyItemInserted(0);
                            isIconSelected = true;
                            validateFields();
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.image_format));
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }

    private void validateFields() {

        if (isNullOrEmpty(zone.toLowerCase().trim())
                || isNullOrEmpty(boardName.getText().toString().trim())
                || isNullOrEmpty(boardDescription.getText().toString().trim())
                || isNullOrEmpty(boardColorThemeAdapter.getSelectedColor())
                || !isIconSelected || !isZoneSelected) {
            createPrivateBoard.setClickable(false);
            createPrivateBoard.setAlpha(.5f);
        } else {
            createPrivateBoard.setAlpha(1f);
            createPrivateBoard.setClickable(true);
        }
    }

    private void createBoard(Board board, String file) {
        if (isNullOrEmpty(board.getZone())) {
            Toast.makeText(this, R.string.select_zone_for_board, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isNullOrEmpty(board.getDisplayname())) {
            boardName.setError("Please enter a board name", getDrawable(R.drawable.ic_error));
            boardName.requestFocus();
            return;
        }
        if (isNullOrEmpty(board.getDescription())) {
            boardDescription.setError("Please enter the board's description", getDrawable(R.drawable.ic_error));
            boardDescription.requestFocus();
            return;
        }
        if (isNullOrEmpty(board.getColor())) {
            Toast.makeText(this, R.string.select_board_color, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isNullOrEmpty(file)) {
            Toast.makeText(this, R.string.select_image_to_upload, Toast.LENGTH_SHORT).show();
            return;
        }

        parentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
        BoardPreviewActivity.launch(this, gson.toJson(board), file);
    }
}