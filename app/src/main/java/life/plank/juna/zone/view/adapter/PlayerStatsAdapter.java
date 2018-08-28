package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.PlayerStatsModel;

public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.MatchLeagueViewHolder> {

    private List<PlayerStatsModel> playerStatsModels;

    public PlayerStatsAdapter() {
        this.playerStatsModels = new ArrayList<>();
    }

    @Override
    public MatchLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.player_stats_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchLeagueViewHolder holder, int position) {
        holder.playerStatsSerialNumberText.setText(String.valueOf(playerStatsModels.get(position).getId()));
        holder.playerStatsPlayerNameText.setText(String.valueOf(playerStatsModels.get(position).getFullName()));
        holder.playerStatsGoalsText.setText(String.valueOf(playerStatsModels.get(position).getGoals()));
        holder.playerStatsAssistTextView.setText(String.valueOf(playerStatsModels.get(position).getAssists()));
        holder.playerStatsYellowTextView.setText(String.valueOf(playerStatsModels.get(position).getYellowcards()));
        holder.playerStatsRedCardTextView.setText(String.valueOf(playerStatsModels.get(position).getRedcards()));
    }

    @Override
    public int getItemCount() {
        return playerStatsModels.size();
    }

    public void update(List<PlayerStatsModel> playerStatsModelList) {
        this.playerStatsModels.addAll(playerStatsModelList);
        notifyDataSetChanged();
    }

    public class MatchLeagueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.player_stats_serial_number_text)
        TextView playerStatsSerialNumberText;
        @BindView(R.id.player_stats_team_logo)
        ImageView playerStatsTeamLogoImage;
        @BindView(R.id.player_stats_player_name)
        TextView playerStatsPlayerNameText;
        @BindView(R.id.player_stats_goals_text)
        TextView playerStatsGoalsText;
        @BindView(R.id.player_stats_assist_text)
        TextView playerStatsAssistTextView;
        @BindView(R.id.player_stats_yellow_card_text)
        TextView playerStatsYellowTextView;
        @BindView(R.id.player_stats_red_card_text)
        TextView playerStatsRedCardTextView;

        MatchLeagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}