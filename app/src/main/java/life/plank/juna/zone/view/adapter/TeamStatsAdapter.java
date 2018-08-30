package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private Picasso picasso;

    public TeamStatsAdapter(Picasso picasso) {
        this.picasso = picasso;
        this.teamStatsModelList = new ArrayList<>();
    }

    @Override
    public TeamStateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_stats_row, parent, false);
        return new TeamStateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamStateViewHolder holder, int position) {
        holder.teamStatsSerialNumber.setText(String.valueOf(position + 1));
        holder.teamStatsTeamNameTextView.setText(String.valueOf(teamStatsModelList.get(position).getTeamName()));
        holder.teamStatsWinsTextView.setText(String.valueOf(teamStatsModelList.get(position).getWin()));
        holder.teamStatsLossesTextView.setText(String.valueOf(teamStatsModelList.get(position).getLoss()));
        holder.teamsStatsGoalTextView.setText(String.valueOf(teamStatsModelList.get(position).getGoal()));
        holder.teamStatsPassTextView.setText(String.valueOf(teamStatsModelList.get(position).getPass()));
        holder.teamsStatsShotTextView.setText(String.valueOf(teamStatsModelList.get(position).getShot()));
        holder.teamStatsRedCardTextView.setText(String.valueOf(teamStatsModelList.get(position).getRedCard()));
        holder.teamStatsYellowCardTextView.setText(String.valueOf(teamStatsModelList.get(position).getYellowCard()));
        picasso.load(teamStatsModelList.get(position).getFootballTeamLogo())
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.teamStatsTeamLogo);
    }

    @Override
    public int getItemCount() {
        return teamStatsModelList.size();
    }

    public void update(List<TeamStatsModel> teamStatsModelList) {
        this.teamStatsModelList.addAll(teamStatsModelList);
        notifyDataSetChanged();
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
        @BindView(R.id.team_stats_pass_text_view)
        TextView teamStatsPassTextView;
        @BindView(R.id.team_stats_goals_text_view)
        TextView teamsStatsGoalTextView;
        @BindView(R.id.team_stats_shot_text_view)
        TextView teamsStatsShotTextView;
        @BindView(R.id.team_stats_serial_number_text_view)
        TextView teamStatsSerialNumber;
        @BindView(R.id.team_stats_yellow_card)
        TextView teamStatsYellowCardTextView;
        @BindView(R.id.team_stats_red_card)
        TextView teamStatsRedCardTextView;

        TeamStateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}