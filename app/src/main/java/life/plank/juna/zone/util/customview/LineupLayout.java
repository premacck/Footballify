package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
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
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.view.adapter.SubstitutionAdapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static life.plank.juna.zone.util.DataUtil.extractSubstitutionEvents;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
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

    @BindView(R.id.substitution_recycler_view)
    RecyclerView substitutionRecyclerView;
    @BindView(R.id.no_substitutions_yet)
    TextView noSubstitutionsYet;
    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogo;
    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogo;
    @BindView(R.id.home_team_logo_under_manager)
    ImageView homeTeamLogoUnderManager;
    @BindView(R.id.visiting_team_logo_under_manager)
    ImageView visitingTeamLogoUnderManager;

    @BindView(R.id.home_player_name)
    TextView homeManager;
    @BindView(R.id.visiting_player_name)
    TextView visitingManager;

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

    public void update(Lineups lineups, String homeLogo, String visitingLogo, Picasso picasso, String homeTeamName, String visitingTeamName) {
        progressBar.setVisibility(GONE);
        noDataTextView.setVisibility(GONE);
        lineupCenterLines.setVisibility(VISIBLE);

        setHomeTeamName(homeTeamName);
        setVisitingTeamName(visitingTeamName);
        setHomeTeamLineup(getLineupText(lineups.getHomeTeamFormation()));
        setVisitingTeamLineup(getLineupText(lineups.getAwayTeamFormation()));
        setHomeManager(lineups.getHomeManagerName());
        setVisitingManager(lineups.getVisitingManagerName());

        prepareLineup(homeTeamLineupLayout, lineups.getHomeTeamFormation(), R.color.lineup_player_red, true);
        prepareLineup(visitingTeamLineupLayout, lineups.getAwayTeamFormation(), R.color.purple, false);

        Target homeTarget = getStartDrawableTarget(this.homeTeamName);
        Target visitingTarget = getStartDrawableTarget(this.visitingTeamName);

        loadImage(picasso, homeLogo, homeTarget);
        loadImage(picasso, visitingLogo, visitingTarget);
        loadImage(picasso, homeLogo, homeTeamLogo);
        loadImage(picasso, visitingLogo, visitingTeamLogo);
        loadImage(picasso, homeLogo, homeTeamLogoUnderManager);
        loadImage(picasso, visitingLogo, visitingTeamLogoUnderManager);
    }

    private void loadImage(Picasso picasso, String logo, Target target) {
        picasso.load(logo)
                .resize((int) getDp(getContext(), 14), (int) getDp(getContext(), 14))
                .into(target);
    }

    private void loadImage(Picasso picasso, String logo, ImageView target) {
        picasso.load(logo)
                .resize((int) getDp(getContext(), 14), (int) getDp(getContext(), 14))
                .into(target);
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
                text.append(" - ");
            }
        }
        return text.toString();
    }

    public String getHomeTeamName() {
        return homeTeamName.getText().toString();
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName.setText(homeTeamName);
    }

    public String getHomeTeamLineup() {
        return homeTeamLineup.getText().toString();
    }

    public void setHomeTeamLineup(String homeTeamLineup) {
        this.homeTeamLineup.setText(homeTeamLineup);
    }

    public String getVisitingTeamName() {
        return visitingTeamName.getText().toString();
    }

    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName.setText(visitingTeamName);
    }

    public String getVisitingTeamLineup() {
        return visitingTeamLineup.getText().toString();
    }

    public void setVisitingTeamLineup(String visitingTeamLineup) {
        this.visitingTeamLineup.setText(visitingTeamLineup);
    }

    public TextView getHomeManager() {
        return homeManager;
    }

    public void setHomeManager(String homeManager) {
        this.homeManager.setText(homeManager);
    }

    public TextView getVisitingManager() {
        return visitingManager;
    }

    public void setVisitingManager(String visitingManager) {
        this.visitingManager.setText(visitingManager);
    }

    public void updateEvents(List<MatchEvent> matchEvents) {
        List<MatchEvent> newMatchEventList = extractSubstitutionEvents(matchEvents);
        if (!isNullOrEmpty(newMatchEventList)) {
            onMatchStarted();
            ((SubstitutionAdapter) substitutionRecyclerView.getAdapter()).update(newMatchEventList);
        } else {
            onMatchYetToStart();
        }
    }

    private void onMatchStarted() {
        noSubstitutionsYet.setVisibility(GONE);
        substitutionRecyclerView.setVisibility(VISIBLE);
    }

    public void onMatchYetToStart() {
        noSubstitutionsYet.setVisibility(VISIBLE);
        substitutionRecyclerView.setVisibility(GONE);
    }

    public void setAdapter(SubstitutionAdapter substitutionAdapter) {
        substitutionRecyclerView.setAdapter(substitutionAdapter);
    }

    public void updateSubstitutions(List<MatchEvent> substitutionEventList) {
        onMatchStarted();
        ((SubstitutionAdapter) substitutionRecyclerView.getAdapter()).updateNew(substitutionEventList);
    }
}