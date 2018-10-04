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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FootballTeam;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.TeamStats;
import lombok.Data;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class TeamStatsBinder extends ItemBinder<TeamStatsBinder.TeamStatsBindingModel, TeamStatsBinder.TeamStatsViewHolder> {

    private Picasso picasso;

    public TeamStatsBinder(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public TeamStatsViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new TeamStatsViewHolder(inflater.inflate(R.layout.item_team_stats, parent, false));
    }

    @Override
    public void bind(TeamStatsViewHolder holder, TeamStatsBindingModel item) {
        holder.progressBar.setVisibility(View.GONE);
        if (item.getErrorMessage() != null || isNullOrEmpty(item.getTeamStatsList())) {
            holder.noDataTextView.setText(item.getErrorMessage());
            holder.noDataTextView.setVisibility(View.VISIBLE);
            holder.teamsLogoLayout.setVisibility(View.GONE);
            holder.matchTeamStatsLayout.setVisibility(View.GONE);
            return;
        }
        if (item.getTeamStatsList().size() < 2) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        TeamStats homeTeamStats = item.getTeamStatsList().get(0);
        TeamStats visitingTeamStats = item.getTeamStatsList().get(1);

        holder.noDataTextView.setVisibility(View.GONE);
        holder.teamsLogoLayout.setVisibility(View.VISIBLE);
        holder.matchTeamStatsLayout.setVisibility(View.VISIBLE);
        holder.leagueName.setText(item.getLeague().getName());

        holder.homeTeamShots.setText(String.valueOf(homeTeamStats.getShot()));
        holder.homeTeamWin.setText(String.valueOf(homeTeamStats.getWin()));
        holder.homeTeamLoss.setText(String.valueOf(homeTeamStats.getLoss()));
        holder.homeTeamGoals.setText(String.valueOf(homeTeamStats.getGoal()));
        holder.homeTeamYellowCard.setText(String.valueOf(homeTeamStats.getYellowCard()));
        holder.homeTeamRedCard.setText(String.valueOf(homeTeamStats.getRedCard()));
        holder.homeTeamPasses.setText(String.valueOf(homeTeamStats.getPass()));

        holder.visitingTeamShots.setText(String.valueOf(visitingTeamStats.getShot()));
        holder.visitingTeamWin.setText(String.valueOf(visitingTeamStats.getWin()));
        holder.visitingTeamLoss.setText(String.valueOf(visitingTeamStats.getLoss()));
        holder.visitingTeamGoals.setText(String.valueOf(visitingTeamStats.getGoal()));
        holder.visitingTeamYellowCard.setText(String.valueOf(visitingTeamStats.getYellowCard()));
        holder.visitingTeamRedCard.setText(String.valueOf(visitingTeamStats.getRedCard()));
        holder.visitingTeamPasses.setText(String.valueOf(visitingTeamStats.getPass()));

        loadLogo(item.getHomeTeam().getLogoLink(), holder.homeTeamLogoImageView);
        loadLogo(item.getAwayTeam().getLogoLink(), holder.visitingTeamLogoImageView);
    }

    private void loadLogo(String url, ImageView imageView) {
        picasso.load(url)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize((int) getDp(14), (int) getDp(14))
                .into(imageView);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof TeamStatsBindingModel;
    }

    static class TeamStatsViewHolder extends ItemViewHolder<TeamStatsBindingModel> {

        @BindView(R.id.teams_logo_layout)
        RelativeLayout teamsLogoLayout;
        @BindView(R.id.match_team_stats_layout)
        LinearLayout matchTeamStatsLayout;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.league_name)
        TextView leagueName;
        @BindView(R.id.no_data)
        TextView noDataTextView;

        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogoImageView;
        @BindView(R.id.home_team_win)
        TextView homeTeamWin;
        @BindView(R.id.home_team_loss)
        TextView homeTeamLoss;
        @BindView(R.id.home_team_goal)
        TextView homeTeamGoals;
        @BindView(R.id.home_team_passes)
        TextView homeTeamPasses;
        @BindView(R.id.home_team_shots)
        TextView homeTeamShots;
        @BindView(R.id.home_team_yellow_card)
        TextView homeTeamYellowCard;
        @BindView(R.id.home_team_red_card)
        TextView homeTeamRedCard;

        @BindView(R.id.visiting_team_logo)
        ImageView visitingTeamLogoImageView;
        @BindView(R.id.visiting_team_win)
        TextView visitingTeamWin;
        @BindView(R.id.visiting_team_loss)
        TextView visitingTeamLoss;
        @BindView(R.id.visiting_team_goal)
        TextView visitingTeamGoals;
        @BindView(R.id.visiting_team_passes)
        TextView visitingTeamPasses;
        @BindView(R.id.visiting_team_shots)
        TextView visitingTeamShots;
        @BindView(R.id.visiting_team_yellow_card)
        TextView visitingTeamYellowCard;
        @BindView(R.id.visiting_team_red_card)
        TextView visitingTeamRedCard;

        TeamStatsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class TeamStatsBindingModel {
        private final List<TeamStats> teamStatsList;
        private final League league;
        private final FootballTeam homeTeam;
        private final FootballTeam awayTeam;
        @StringRes
        private final Integer errorMessage;

        public static TeamStatsBindingModel from(MatchDetails matchDetails) {
            return new TeamStatsBindingModel(
                    matchDetails.getTeamStatsList(),
                    matchDetails.getLeague(),
                    matchDetails.getHomeTeam(),
                    matchDetails.getAwayTeam(),
                    isNullOrEmpty(matchDetails.getTeamStatsList()) ? R.string.team_stats_not_available_yet : null
            );
        }
    }
}
