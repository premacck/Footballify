package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class PostCommentActivity extends AppCompatActivity {
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    @BindView(R.id.comment_text_view)
    TextView commentTextView;

    @BindView(R.id.red)
    ImageView redBg;
    @BindView(R.id.pink)
    ImageView pinkBg;
    @BindView(R.id.yellow)
    ImageView yellowBg;
    @BindView(R.id.green)
    ImageView greenBg;
    @BindView(R.id.blue)
    ImageView blueBg;

    @BindView(R.id.comment_card_view)
    CardView commentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_comment);
        ButterKnife.bind(this);

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                commentTextView.setText(commentEditText.getText().toString());
            }
        });

        redBg.setOnClickListener(v -> setColor(R.color.red));
        pinkBg.setOnClickListener(v -> setColor(R.color.material_pink_800));
        yellowBg.setOnClickListener(v -> setColor(R.color.material_yellow_700));
        greenBg.setOnClickListener(v -> setColor(R.color.material_green_700));
        blueBg.setOnClickListener(v -> setColor(R.color.material_blue_600));

    }

    private void setColor(int color) {
        commentCardView.setCardBackgroundColor(getResources().getColor(color));
    }
}
