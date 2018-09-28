package life.plank.juna.zone.view.adapter.multiview.binder;

import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballTeam;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchStats;
import life.plank.juna.zone.data.network.model.Stadium;
import lombok.Data;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class MatchStatsBinder extends ItemBinder<MatchStatsBinder.MatchStatsBindingModel, MatchStatsBinder.MatchStatsViewHolder> {

    private Picasso picasso;

    public MatchStatsBinder(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public MatchStatsViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new MatchStatsViewHolder(inflater.inflate(R.layout.item_match_stats, parent, false));
    }

    @Override
    public void bind(MatchStatsViewHolder holder, MatchStatsBindingModel item) {
        holder.progressBar.setVisibility(View.GONE);
        if (item.getErrorMessage() != null) {
            holder.noDataTextView.setText(item.getErrorMessage());
            holder.noDataTextView.setVisibility(View.VISIBLE);
            holder.teamsLogoLayout.setVisibility(View.INVISIBLE);
            holder.matchTeamStatsLayout.setVisibility(View.INVISIBLE);
            return;
        }

        holder.noDataTextView.setVisibility(View.GONE);
        holder.teamsLogoLayout.setVisibility(View.VISIBLE);
        holder.matchTeamStatsLayout.setVisibility(View.VISIBLE);

        holder.venueName.setText(item.getVenue().getName());
        holder.homeTeamShots.setText(String.valueOf(item.getMatchStats().getHomeShots()));
        holder.homeTeamShotsOnTarget.setText(String.valueOf(item.getMatchStats().getHomeShotsOnTarget()));
        String homeTeamPossession = item.getMatchStats().getHomePossession() + "%";
        holder.homeTeamPossession.setText(String.valueOf(homeTeamPossession));
        holder.homeTeamFouls.setText(String.valueOf(item.getMatchStats().getHomeFouls()));
        holder.homeTeamYellowCard.setText(String.valueOf(item.getMatchStats().getHomeYellowCards()));
        holder.homeTeamRedCard.setText(String.valueOf(item.getMatchStats().getHomeRedCards()));
        holder.homeTeamOffside.setText(String.valueOf(item.getMatchStats().getHomeOffsides()));
        holder.homeTeamCorner.setText(String.valueOf(item.getMatchStats().getHomeCorners()));

        holder.visitingTeamShots.setText(String.valueOf(item.getMatchStats().getAwayShots()));
        holder.visitingTeamShotsOnTarget.setText(String.valueOf(item.getMatchStats().getAwayShotsOnTarget()));
        String visitingTeamPossession = item.getMatchStats().getAwayPossession() + "%";
        holder.visitingTeamPossession.setText(visitingTeamPossession);
        holder.visitingTeamFouls.setText(String.valueOf(item.getMatchStats().getAwayFouls()));
        holder.visitingTeamYellowCard.setText(String.valueOf(item.getMatchStats().getAwayYellowCards()));
        holder.visitingTeamRedCard.setText(String.valueOf(item.getMatchStats().getAwayRedCards()));
        holder.visitingTeamOffside.setText(String.valueOf(item.getMatchStats().getAwayOffsides()));
        holder.visitingTeamCorner.setText(String.valueOf(item.getMatchStats().getAwayCorners()));

        picasso.load(item.getHomeTeam().getLogoLink())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize((int) getDp(14), (int) getDp(14))
                .into(holder.homeTeamLogoImageView);
        picasso.load(item.getAwayTeam().getLogoLink())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize((int) getDp(14), (int) getDp(14))
                .into(holder.visitingTeamLogoImageView);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof MatchStatsBindingModel;
    }

    static class MatchStatsViewHolder extends ItemViewHolder<MatchStatsBindingModel> {

        @BindView(R.id.teams_logo_layout)
        RelativeLayout teamsLogoLayout;
        @BindView(R.id.match_team_stats_layout)
        LinearLayout matchTeamStatsLayout;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.venue_name)
        TextView venueName;
        @BindView(R.id.no_data)
        TextView noDataTextView;

        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogoImageView;
        @BindView(R.id.home_team_shots)
        TextView homeTeamShots;
        @BindView(R.id.home_team_shots_on_target)
        TextView homeTeamShotsOnTarget;
        @BindView(R.id.home_team_possession)
        TextView homeTeamPossession;
        @BindView(R.id.home_team_fouls)
        TextView homeTeamFouls;
        @BindView(R.id.home_team_yellow_card)
        TextView homeTeamYellowCard;
        @BindView(R.id.home_team_red_card)
        TextView homeTeamRedCard;
        @BindView(R.id.home_team_offside)
        TextView homeTeamOffside;
        @BindView(R.id.home_team_corner)
        TextView homeTeamCorner;

        @BindView(R.id.visiting_team_logo)
        ImageView visitingTeamLogoImageView;
        @BindView(R.id.visiting_team_shots)
        TextView visitingTeamShots;
        @BindView(R.id.visiting_team_shots_on_target)
        TextView visitingTeamShotsOnTarget;
        @BindView(R.id.visiting_team_possession)
        TextView visitingTeamPossession;
        @BindView(R.id.visiting_team_fouls)
        TextView visitingTeamFouls;
        @BindView(R.id.visiting_team_yellow_card)
        TextView visitingTeamYellowCard;
        @BindView(R.id.visiting_team_red_card)
        TextView visitingTeamRedCard;
        @BindView(R.id.visiting_team_offside)
        TextView visitingTeamOffside;
        @BindView(R.id.visiting_team_corner)
        TextView visitingTeamCorner;

        MatchStatsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class MatchStatsBindingModel {
        private final MatchStats matchStats;
        private final Stadium venue;
        private final FootballTeam homeTeam;
        private final FootballTeam awayTeam;
        @StringRes
        private final Integer errorMessage;

        public static MatchStatsBindingModel from(MatchDetails matchDetails) {
            return new MatchStatsBindingModel(
                    matchDetails.getMatchStats(),
                    matchDetails.getVenue(),
                    matchDetails.getHomeTeam(),
                    matchDetails.getAwayTeam(),
                    matchDetails.getMatchStats() == null ? R.string.match_stats_not_available_yet : null
            );
        }
    }
}
