package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.view.adapter.SubstitutionAdapter;

import static life.plank.juna.zone.util.DataUtil.extractSubstitutionEvents;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class SubstitutionLayout extends FrameLayout {

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

    public SubstitutionLayout(@NonNull Context context) {
        this(context, null);
    }

    public SubstitutionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubstitutionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SubstitutionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_substitution_layout, this);
        ButterKnife.bind(this, rootView);
    }

    public void update(Lineups lineups, String homeLogo, String visitingLogo, Picasso picasso) {
        setHomeManager(lineups.getHomeManagerName());
        setVisitingManager(lineups.getVisitingManagerName());

        loadImage(picasso, homeLogo, homeTeamLogo);
        loadImage(picasso, visitingLogo, visitingTeamLogo);
        loadImage(picasso, homeLogo, homeTeamLogoUnderManager);
        loadImage(picasso, visitingLogo, visitingTeamLogoUnderManager);
    }

    public void setLoading(boolean isLoading) {
        setVisibility(isLoading ? GONE : VISIBLE);
    }

    private void loadImage(Picasso picasso, String logo, ImageView target) {
        picasso.load(logo)
                .resize((int) getDp(getContext(), 14), (int) getDp(getContext(), 14))
                .into(target);
    }

    public void setHomeManager(String homeManager) {
        this.homeManager.setText(homeManager);
    }

    public void setVisitingManager(String visitingManager) {
        this.visitingManager.setText(visitingManager);
    }

    public void update(List<MatchEvent> matchEvents, String homeLogo, String visitingLogo, Picasso picasso) {
        List<MatchEvent> newMatchEventList = extractSubstitutionEvents(matchEvents);
        if (!isNullOrEmpty(newMatchEventList)) {
            onMatchStarted();
            ((SubstitutionAdapter) substitutionRecyclerView.getAdapter()).update(newMatchEventList);
        } else {
            onMatchYetToStart();
        }
        loadImage(picasso, homeLogo, homeTeamLogo);
        loadImage(picasso, visitingLogo, visitingTeamLogo);
        loadImage(picasso, homeLogo, homeTeamLogoUnderManager);
        loadImage(picasso, visitingLogo, visitingTeamLogoUnderManager);
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