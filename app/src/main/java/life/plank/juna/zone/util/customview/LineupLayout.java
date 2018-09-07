package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchFixture;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTarget;

public class LineupLayout extends FrameLayout {

    @BindView(R.id.home_team_name)
    TextView homeTeamName;
    @BindView(R.id.home_team_lineup_text)
    TextView homeTeamLineup;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noDataTextView;
    @BindView(R.id.lineup_center_lines)
    ImageView lineupCenterLines;
    @BindView(R.id.home_team_lineup_layout)
    LinearLayout homeTeamLineupLayout;
    @BindView(R.id.visiting_team_lineup_layout)
    LinearLayout visitingTeamLineupLayout;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;
    @BindView(R.id.visiting_team_lineup_text)
    TextView visitingTeamLineup;

    public LineupLayout(@NonNull Context context) {
        this(context, null);
    }

    public LineupLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineupLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineupLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_line_up, this);
        ButterKnife.bind(this, rootView);
    }

    public void update(Lineups lineups, MatchFixture fixture, Picasso picasso) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        lineupCenterLines.setVisibility(VISIBLE);

        setHomeTeamName(fixture.getHomeTeam().getName());
        setVisitingTeamName(fixture.getAwayTeam().getName());
        setHomeTeamLineup(getLineupText(lineups.getHomeTeamFormation()));
        setVisitingTeamLineup(getLineupText(lineups.getAwayTeamFormation()));

        prepareLineup(homeTeamLineupLayout, lineups.getHomeTeamFormation(), R.color.lineup_player_red, true);
        prepareLineup(visitingTeamLineupLayout, lineups.getAwayTeamFormation(), R.color.purple, false);

        Target homeTarget = getStartDrawableTarget(this.homeTeamName);
        Target visitingTarget = getStartDrawableTarget(this.visitingTeamName);

        loadImage(picasso, fixture.getHomeTeam().getLogoLink(), homeTarget);
        loadImage(picasso, fixture.getAwayTeam().getLogoLink(), visitingTarget);
    }

    private void loadImage(Picasso picasso, String logo, Target target) {
        picasso.load(logo)
                .resize((int) getDp(getContext(), 14), (int) getDp(getContext(), 14))
                .into(target);
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    public void notAvailable(@StringRes int message) {
        noDataTextView.setText(message);
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(VISIBLE);
        lineupCenterLines.setVisibility(INVISIBLE);
    }

    public void prepareLineup(LinearLayout lineupLayout, List<List<Lineups.Formation>> formationsList, @ColorRes int labelColor, boolean isHomeTeam) {
        if (!isHomeTeam) {
            Collections.reverse(formationsList);
        }
        for (List<Lineups.Formation> formations : formationsList) {
            lineupLayout.addView(getLineupLayoutLine(formations, labelColor));
        }
    }

    private LinearLayout getLineupLayoutLine(List<Lineups.Formation> formations, @ColorRes int labelColor) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(params);
        for (Lineups.Formation formation : formations) {
            linearLayout.addView(new LineupPlayer(getContext(), formation, labelColor));
        }
        return linearLayout;
    }

    private String getLineupText(List<List<Lineups.Formation>> formationsList) {
        StringBuilder text = new StringBuilder();
        for (List<Lineups.Formation> formations : formationsList.subList(1, formationsList.size())) {
            text.append(formations.size());
            if (formationsList.indexOf(formations) < formationsList.size() - 1) {
                text.append(DASH);
            }
        }
        return text.toString();
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName.setText(homeTeamName);
    }

    public void setHomeTeamLineup(String homeTeamLineup) {
        this.homeTeamLineup.setText(homeTeamLineup);
    }

    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName.setText(visitingTeamName);
    }

    public void setVisitingTeamLineup(String visitingTeamLineup) {
        this.visitingTeamLineup.setText(visitingTeamLineup);
    }
}