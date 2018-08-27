package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Lineups;

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
    ImageView lineupPlayerSubstitution;
    @BindView(R.id.lineup_player_name)
    TextView lineupPlayerName;

    private Lineups.Formation formation;

    public LineupPlayer(@NonNull Context context) {
        this(context, null, R.color.purple);
    }

    public LineupPlayer(@NonNull Context context, Lineups.Formation formation, @ColorRes int labelColor) {
        this(context, null, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, Lineups.Formation formation, @ColorRes int labelColor) {
        this(context, attrs, 0, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Lineups.Formation formation, @ColorRes int labelColor) {
        this(context, attrs, defStyleAttr, 0, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, Lineups.Formation formation, @ColorRes int labelColor) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.formation = formation;
        init(context, labelColor);
    }

    private void init(Context context, @ColorRes int labelColor) {
        View rootView = inflate(context, R.layout.item_player_in_lineup, this);
        ButterKnife.bind(this, rootView);
        update(labelColor);
    }

    public void update(@ColorRes int labelColor) {
        if (formation != null) {
            setPlayerNumber(formation.getNumber())
//    TODO : replace with boolean values once backend filters it
                    .setPlayerCard(
                            formation.getYellowCard() == 1,
                            formation.getRedCard() == 1,
                            formation.getYellowRed() == 1)
                    .setSubstituted(
                            formation.getSubstituteIn() == 1)
                    .setSolidColor(labelColor)
                    .setPlayerNumber(formation.getNumber())
                    .setGoal(formation.getGoals())
                    .setName(formation.getNickname());
        }
    }

    public LineupPlayer setSolidColor(@ColorRes int labelColor) {
        lineupPlayerNumber.setSolidColor(labelColor);
        return this;
    }

    public int getPlayerNumber() {
        return Integer.parseInt(lineupPlayerNumber.getText().toString());
    }

    public LineupPlayer setPlayerNumber(int number) {
        this.lineupPlayerNumber.setText(String.valueOf(number));
        return this;
    }

    public LineupPlayer setPlayerCard(boolean yellowCard, boolean redCard, boolean yellowRed) {
        if (!yellowCard && !redCard && !yellowRed) {
            lineupPlayerCard.setVisibility(GONE);
            return this;
        }
        lineupPlayerCard.setVisibility(VISIBLE);
        if (yellowRed) {
            lineupPlayerCard.setImageResource(R.drawable.yellow_red);
        } else if (redCard) {
            lineupPlayerCard.setImageResource(R.drawable.red_right);
        } else {
            lineupPlayerCard.setImageResource(R.drawable.yellow_right);
        }
        return this;
    }

    public boolean hasScoredGoal() {
        return lineupPlayerGoal.getVisibility() == VISIBLE;
    }

    public LineupPlayer setGoal(int goalCount) {
        this.lineupPlayerGoal.setVisibility(goalCount > 0 ? VISIBLE : GONE);
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

    public LineupPlayer setSubstituted(boolean isSubstitutedIn) {
        this.lineupPlayerSubstitution.setVisibility(isSubstitutedIn ? GONE : VISIBLE);
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