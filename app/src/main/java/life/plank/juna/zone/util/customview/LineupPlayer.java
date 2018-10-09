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

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.Formation;

public class LineupPlayer extends FrameLayout {

    @BindView(R.id.lineup_player_pic)
    CircleImageView lineupPlayerPic;
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

    private Formation formation;

    public LineupPlayer(@NonNull Context context) {
        this(context, null, R.color.purple);
    }

    public LineupPlayer(@NonNull Context context, Formation formation, @ColorRes int labelColor) {
        this(context, null, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, Formation formation, @ColorRes int labelColor) {
        this(context, attrs, 0, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Formation formation, @ColorRes int labelColor) {
        this(context, attrs, defStyleAttr, 0, formation, labelColor);
        this.formation = formation;
    }

    public LineupPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, Formation formation, @ColorRes int labelColor) {
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
                            formation.getYellowCards(),
                            formation.getRedCards(),
                            formation.getYellowRed())
                    .setSubstituted(formation.getSubstituteOut())
                    .setSolidColor(labelColor)
                    .setPlayerNumber(formation.getNumber())
                    .setGoal(formation.getGoals())
                    .setName(formation.getNickname());
            Picasso.with(getContext())
                    .load(formation.getImagePath())
                    .placeholder(R.drawable.ic_default_profile)
                    .error(R.drawable.ic_default_profile)
                    .into(lineupPlayerPic);
        }
    }

    public LineupPlayer setSolidColor(@ColorRes int labelColor) {
        lineupPlayerNumber.setSolidColor(labelColor);
        return this;
    }

    public LineupPlayer setPlayerNumber(int number) {
        this.lineupPlayerNumber.setText(String.valueOf(number));
        return this;
    }

    public LineupPlayer setPlayerCard(int yellowCard, int redCard, int yellowRed) {
        if (yellowCard == 0 && redCard == 0 && yellowRed == 0) {
            lineupPlayerCard.setVisibility(GONE);
            return this;
        }
        lineupPlayerCard.setVisibility(VISIBLE);
        if (yellowRed == 1) {
            lineupPlayerCard.setImageResource(R.drawable.yellow_red);
        } else if (redCard == 1) {
            lineupPlayerCard.setImageResource(R.drawable.red_right);
        } else if (yellowCard == 1) {
            lineupPlayerCard.setImageResource(R.drawable.yellow_right);
        }
        return this;
    }

    public LineupPlayer setGoal(int goalCount) {
        this.lineupPlayerGoal.setVisibility(goalCount > 0 ? VISIBLE : GONE);
        setGoalCount(goalCount);
        this.lineupPlayerGoalCount.setVisibility(goalCount > 1 ? VISIBLE : GONE);
        return this;
    }

    public void setGoalCount(int goalCount) {
        this.lineupPlayerGoalCount.setText(String.valueOf(goalCount));
    }

    public LineupPlayer setSubstituted(int isSubstitutedIn) {
        this.lineupPlayerSubstitution.setVisibility(isSubstitutedIn == 1 ? VISIBLE : GONE);
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