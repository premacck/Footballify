package life.plank.juna.zone.view.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class CreatePrivateBoardActivity extends AppCompatActivity {

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

    @BindView(R.id.red)
    TextView red;
    @BindView(R.id.yellow)
    TextView yellow;
    @BindView(R.id.purple)
    TextView purple;
    @BindView(R.id.pink)
    TextView pink;
    @BindView(R.id.green)
    TextView green;
    @BindView(R.id.blue)
    TextView blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_board);
        ButterKnife.bind(this);

        football.setOnClickListener(v -> toggleView(football));
        music.setOnClickListener(v -> toggleView(music));
        drama.setOnClickListener(v -> toggleView(drama));
        tune.setOnClickListener(v -> toggleView(tune));
        skill.setOnClickListener(v -> toggleView(skill));
        other.setOnClickListener(v -> toggleView(other));

        setColor(red, getColor(R.color.red));
        setColor(yellow, getColor(R.color.material_yellow_700));
        setColor(purple, getColor(R.color.purple_toolbar_background));
        setColor(pink, getColor(R.color.fab_button_pink));
        setColor(green, getColor(R.color.green));
        setColor(blue, getColor(R.color.com_facebook_blue));
    }

    public void toggleView(ToggleButton view) {
        if (view.isChecked()) {
            view.setBackground(getDrawable(R.drawable.unselected_text_view_bg));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            view.setTextColor(getColor(R.color.grey));
        } else {
            view.setBackground(getDrawable(R.drawable.selected_textview_bg));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_tag, 0);
            view.setTextColor(getColor(R.color.white));
        }
    }

    public void setColor(TextView view, int color) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(color);
    }
}
