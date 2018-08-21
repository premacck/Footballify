package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class LineUpLayout extends FrameLayout {

    @BindView(R.id.home_team_name)
    TextView homeTeamName;
    @BindView(R.id.home_team_lineup)
    TextView homeTeamLineup;
    @BindView(R.id.home_goalee)
    LineupPlayer homeGoalee;

    @BindView(R.id.home_defenders)
    LinearLayout homeDefenders;
    @BindView(R.id.home_mid_fielders)
    LinearLayout homeMidFielders;
    @BindView(R.id.home_attackers)
    LinearLayout homeAttackers;

    @BindView(R.id.visiting_attackers)
    LinearLayout visitingAttackers;
    @BindView(R.id.visiting_mid_fielders)
    LinearLayout visitingMidFielders;
    @BindView(R.id.visiting_defenders)
    LinearLayout visitingDefenders;

    @BindView(R.id.visiting_team_name)
    TextView visitingTeamName;
    @BindView(R.id.visiting_team_lineup)
    TextView visitingTeamLineup;
    @BindView(R.id.visiting_goalee)
    LineupPlayer visitingGoalee;

    @BindView(R.id.substitution_list)
    RecyclerView substitutionList;

    @BindView(R.id.home_player_name)
    TextView homeManager;
    @BindView(R.id.visiting_player_name)
    TextView visitingManager;

    List<LineupPlayer> homeDefenderList = new ArrayList<>();
    List<LineupPlayer> homeMidFielderList = new ArrayList<>();
    List<LineupPlayer> homeAttackerList = new ArrayList<>();

    List<LineupPlayer> visitingAttackerList = new ArrayList<>();
    List<LineupPlayer> visitingMidFielderList = new ArrayList<>();
    List<LineupPlayer> visitingDefenderList = new ArrayList<>();

    public LineUpLayout(@NonNull Context context) {
        this(context, null);
    }

    public LineUpLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineUpLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineUpLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_line_up, this);
        ButterKnife.bind(this, rootView);
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

    public LineupPlayer getHomeGoalee() {
        return homeGoalee;
    }

    public void setHomeGoalee(LineupPlayer homeGoalee) {
        this.homeGoalee = homeGoalee;
    }

    public LineupPlayer getVisitingGoalee() {
        return visitingGoalee;
    }

    public void setVisitingGoalee(LineupPlayer visitingGoalee) {
        this.visitingGoalee = visitingGoalee;
    }

    public List<LineupPlayer> getHomeDefenderList() {
        return homeDefenderList;
    }

    public void setHomeDefenders(List<LineupPlayer> homeDefenderList) {
        this.homeDefenderList = homeDefenderList;
        populateLineUp(homeDefenderList, homeDefenders);
    }

    public List<LineupPlayer> getHomeMidFielderList() {
        return homeMidFielderList;
    }

    public void setHomeMidFielderList(List<LineupPlayer> homeMidFielderList) {
        this.homeMidFielderList = homeMidFielderList;
        populateLineUp(homeMidFielderList, homeMidFielders);
    }

    public List<LineupPlayer> getHomeAttackerList() {
        return homeAttackerList;
    }

    public void setHomeAttackerList(List<LineupPlayer> homeAttackerList) {
        this.homeAttackerList = homeAttackerList;
        populateLineUp(homeAttackerList, homeAttackers);
    }

    public List<LineupPlayer> getVisitingAttackerList() {
        return visitingAttackerList;
    }

    public void setVisitingAttackerList(List<LineupPlayer> visitingAttackerList) {
        this.visitingAttackerList = visitingAttackerList;
        populateLineUp(visitingAttackerList, visitingAttackers);
    }

    public List<LineupPlayer> getVisitingMidFielderList() {
        return visitingMidFielderList;
    }

    public void setVisitingMidFielderList(List<LineupPlayer> visitingMidFielderList) {
        this.visitingMidFielderList = visitingMidFielderList;
        populateLineUp(visitingMidFielderList, visitingMidFielders);
    }

    public List<LineupPlayer> getVisitingDefenderList() {
        return visitingDefenderList;
    }

    public void setVisitingDefenderList(List<LineupPlayer> visitingDefenderList) {
        this.visitingDefenderList = visitingDefenderList;
        populateLineUp(visitingDefenderList, visitingDefenders);
    }

    public TextView getVisitingTeamName() {
        return visitingTeamName;
    }

    public void setVisitingTeamName(TextView visitingTeamName) {
        this.visitingTeamName = visitingTeamName;
    }

    public String getVisitingTeamLineup() {
        return visitingTeamLineup.getText().toString();
    }

    public void setVisitingTeamLineup(String visitingTeamLineup) {
        this.visitingTeamLineup.setText(visitingTeamLineup);
    }

    public RecyclerView getSubstitutionList() {
        return substitutionList;
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

    private void populateLineUp(List<LineupPlayer> lineupPlayers, LinearLayout lineupSectionLayout) {
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            lineupSectionLayout.addView(lineupPlayer);
        }
    }
}