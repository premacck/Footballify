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
import life.plank.juna.zone.data.network.model.PlayerStatsModel;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;

public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.MatchLeagueViewHolder> {
    private Context context;
    private List<PlayerStatsModel> playerStatsModels;

    public PlayerStatsAdapter(Context context, List<PlayerStatsModel> playerStatsModelList) {
        this.context = context;
        this.playerStatsModels = playerStatsModelList;
    }

    @Override
    public MatchLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchLeagueViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.player_stats_row, parent, false ) );
    }

    @Override
    public void onBindViewHolder(MatchLeagueViewHolder holder, int position) {
        holder.PlayerStatsSerialNumberText.setText( String.valueOf( playerStatsModels.get( position ).getId() ) );
        holder.PlayerStatsPlayerNameText.setText( String.valueOf( playerStatsModels.get( position ).getFullName() ) );
        holder.PlayerStatsGoalsText.setText( String.valueOf( playerStatsModels.get( position ).getGoals() ) );
        holder.PlayerStatsAssistTextView.setText( String.valueOf( playerStatsModels.get( position ).getAssists() ) );
        holder.PlayerStatsYellowTextView.setText( String.valueOf( playerStatsModels.get( position ).getYellowcards() ) );
        holder.PlayerStatsRedCardTextView.setText( String.valueOf( playerStatsModels.get( position ).getRedcards() ) );
    }

    @Override
    public int getItemCount() {
        return playerStatsModels.size();
    }

    public class MatchLeagueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.player_stats_serial_number_text)
        TextView PlayerStatsSerialNumberText;
        @BindView(R.id.player_stats_team_logo)
        ImageView PlayerStatsTeamLogoImage;
        @BindView(R.id.player_stats_player_name)
        TextView PlayerStatsPlayerNameText;
        @BindView(R.id.player_stats_goals_text)
        TextView PlayerStatsGoalsText;
        @BindView(R.id.player_stats_assist_text)
        TextView PlayerStatsAssistTextView;
        @BindView(R.id.player_stats_yellow_card_text)
        TextView PlayerStatsYellowTextView;
        @BindView(R.id.player_stats_red_card_text)
        TextView PlayerStatsRedCardTextView;

        public MatchLeagueViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}