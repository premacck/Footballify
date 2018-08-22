package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.AppConstants.RED;
import static life.plank.juna.zone.util.AppConstants.YELLOW;

public class LineupPlayer extends FrameLayout {

    @BindView(R.id.lineup_player_number)
    CircularTextView lineupPlayerNumber;
    @BindView(R.id.lineup_player_card)
    ImageView lineupPlayerCard;
    @BindView(R.id.lineup_player_goal)
    ImageView lineupPlayerGoal;
    @BindView(R.id.lineup_player_goal_count)
    CircularTextView lineupPlayerGoalCount;
    @BindView(R.id.lineup_player_substitution)
    CircleImageView lineupPlayerSubstitution;
    @BindView(R.id.lineup_player_name)
    TextView lineupPlayerName;

    private String givenCard = "none";

    public LineupPlayer(@NonNull Context context) {
        this(context, null);
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_player_in_lineup, this);
        ButterKnife.bind(this, rootView);
    }

    public LineupPlayer setSolidColor(int backgroundColor) {
        lineupPlayerNumber.setSolidColor(backgroundColor);
        return this;
    }

    public int getPlayerNumber() {
        return Integer.parseInt(lineupPlayerNumber.getText().toString());
    }

    public LineupPlayer setPlayerNumber(int number) {
        this.lineupPlayerNumber.setText(String.valueOf(number));
        return this;
    }

    public String getPlayerCard() {
        return givenCard;
    }

    public void setPlayerCard(String cardGiven) {
        this.givenCard = cardGiven;
        switch (cardGiven) {
            case YELLOW:
                lineupPlayerCard.setVisibility(VISIBLE);
                lineupPlayerCard.setImageResource(R.drawable.yellow_right);
                break;
            case RED:
                lineupPlayerCard.setVisibility(VISIBLE);
                lineupPlayerCard.setImageResource(R.drawable.red_right);
                break;
            default:
                lineupPlayerCard.setVisibility(GONE);
                break;
        }
    }

    public boolean hasScoredGoal() {
        return lineupPlayerGoal.getVisibility() == VISIBLE;
    }

    public LineupPlayer setGoal(int goalCount) {
        this.lineupPlayerGoal.setVisibility(goalCount > 0 ? GONE : VISIBLE);
        if (goalCount > 1) {
            setGoalCount(goalCount);
        } else {
            lineupPlayerGoalCount.setVisibility(GONE);
        }
        return this;
    }

    public String getGoalCount() {
        return lineupPlayerGoalCount.getText().toString();
    }

    public void setGoalCount(int goalCount) {
        this.lineupPlayerGoalCount.setText(String.valueOf(goalCount));
    }

    public boolean isSubstituted() {
        return lineupPlayerSubstitution.getVisibility() == VISIBLE;
    }

    public LineupPlayer setSubstituted(boolean isSubstituted) {
        this.lineupPlayerSubstitution.setVisibility(isSubstituted ? GONE : VISIBLE);
        return this;
    }

    public String getName() {
        return lineupPlayerName.getText().toString();
    }

    public LineupPlayer setName(String name) {
        this.lineupPlayerName.setText(name);
        return this;
    }
}