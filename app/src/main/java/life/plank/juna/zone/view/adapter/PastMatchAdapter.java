package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.BoardActivity;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES;
import static life.plank.juna.zone.util.PreferenceManager.getMatchPrefs;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class PastMatchAdapter extends RecyclerView.Adapter<PastMatchAdapter.MatchFixtureAndResultViewHolder> {

    private Context context;
    private HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap;

    public PastMatchAdapter(Context context) {
        this.context = context;
        this.classifiedMatchesMap = new HashMap<>();
        classifiedMatchesMap.put(PAST_MATCHES, new ArrayList<>());
    }

    @Override public MatchFixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchFixtureAndResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_fixture, parent, false), this);
    }

    @Override public void onBindViewHolder(MatchFixtureAndResultViewHolder holder, int position) {
        Long matchId = classifiedMatchesMap.get(PAST_MATCHES).get(position).getForeignId();
        holder.dateSchedule.setText(String.valueOf(classifiedMatchesMap.get(PAST_MATCHES).get(position).getMatchStartTime()));
        if (classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeTeam().getLogoLink() != null) {
            Picasso.with(context)
                    .load(classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeTeam().getLogoLink())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_place_holder)
                    .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, context), 0))
                    .error(R.drawable.ic_place_holder)
                    .into(holder.homeTeamLogo);
        }
        if (classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeTeam().getLogoLink() != null) {
            Picasso.with(context)
                    .load(classifiedMatchesMap.get(PAST_MATCHES).get(position).getAwayTeam().getLogoLink())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_place_holder)
                    .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, context), 0))
                    .error(R.drawable.ic_place_holder)
                    .into(holder.awayTeamLogo);
        }

        String teamNameText = classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeTeam().getName() +
                " - " + classifiedMatchesMap.get(PAST_MATCHES).get(position).getAwayTeam().getName();
        holder.teamNames.setText(teamNameText);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BoardActivity.class);

            getMatchPrefs(context).edit()
                    .putLong(context.getString(R.string.match_id_string), matchId)
                    .putString(
                            context.getString(R.string.pref_home_team_logo),
                            classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeTeam().getLogoLink()
                    ).putString(
                            context.getString(R.string.pref_away_team_logo),
                            classifiedMatchesMap.get(PAST_MATCHES).get(position).getAwayTeam().getLogoLink()
                    ).apply();

            intent.putExtra(context.getString(R.string.intent_home_team_score), classifiedMatchesMap.get(PAST_MATCHES).get(position).getHomeGoals().toString());
            intent.putExtra(context.getString(R.string.intent_visiting_team_score), classifiedMatchesMap.get(PAST_MATCHES).get(position).getAwayGoals().toString());
            context.startActivity(intent);
        });
    }

    public void update(HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap) {
        this.classifiedMatchesMap.putAll(classifiedMatchesMap);
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        return classifiedMatchesMap.get(PAST_MATCHES).size();
    }

    public static class MatchFixtureAndResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_team_logo) ImageView homeTeamLogo;
        @BindView(R.id.away_team_logo) ImageView awayTeamLogo;
        @BindView(R.id.date_schedule) TextView dateSchedule;
        @BindView(R.id.team_names) TextView teamNames;

        private final WeakReference<PastMatchAdapter> ref;

        MatchFixtureAndResultViewHolder(View itemView, PastMatchAdapter pastMatchAdapter) {
            super(itemView);
            this.ref = new WeakReference<>(pastMatchAdapter);
            ButterKnife.bind(this, itemView);
        }
    }
}