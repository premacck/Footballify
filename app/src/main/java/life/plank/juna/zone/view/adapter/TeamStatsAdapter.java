package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.TeamStatsModel;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.TeamStateViewHolder> {
    private List<TeamStatsModel> teamStatsModelList;
    private Context context;

    public TeamStatsAdapter(Context context, List<TeamStatsModel> teamStatsModelList) {
        this.context = context;
        this.teamStatsModelList = teamStatsModelList;
    }

    @Override
    public TeamStateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_stats_row, parent, false);
        return new TeamStateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamStateViewHolder holder, int position) {
        holder.teamStatsWinsTextView.setText(String.valueOf(teamStatsModelList.get(position).getTotalWins()));
        holder.teamStatsLossesTextView.setText(String.valueOf(teamStatsModelList.get(position).getTotalLosses()));
        holder.teamsStatsGoalForTextView.setText(String.valueOf(teamStatsModelList.get(position).getTotalGoalsFor()));
        holder.teamStatsDrawTextView.setText(String.valueOf(teamStatsModelList.get(position).getTotalDraws()));
        holder.teamsStatsGoalAgaintsTextView.setText(String.valueOf(teamStatsModelList.get(position).getTotalGoalsAgainst()));
        holder.teamStatsSerialNumber.setText(String.valueOf(teamStatsModelList.get(position).getId()));
        holder.teamStatsTeamNameTextView.setText(String.valueOf(teamStatsModelList.get(position).getFootballTeam().getName()));
        Picasso.with(context)
                .load(teamStatsModelList.get(position).getFootballTeam().getLogoLink())
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.teamStatsTeamLogo);
    }

    @Override
    public int getItemCount() {
        return teamStatsModelList.size();
    }

    public class TeamStateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.team_stats_team_logo)
        ImageView teamStatsTeamLogo;
        @BindView(R.id.team_stats_team_name_text_view)
        TextView teamStatsTeamNameTextView;
        @BindView(R.id.team_stats_wins_text_view)
        TextView teamStatsWinsTextView;
        @BindView(R.id.team_stats_losses_text_view)
        TextView teamStatsLossesTextView;
        @BindView(R.id.team_stats_draw_text_view)
        TextView teamStatsDrawTextView;
        @BindView(R.id.team_stats_goals_for_text_view)
        TextView teamsStatsGoalForTextView;
        @BindView(R.id.team_stats_goal_against_text_view)
        TextView teamsStatsGoalAgaintsTextView;
        @BindView(R.id.team_stats_serial_number_text_view)
        TextView teamStatsSerialNumber;


        TeamStateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}