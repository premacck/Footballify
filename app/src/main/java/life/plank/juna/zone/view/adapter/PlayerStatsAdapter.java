package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.MatchLeagueViewHolder> {
    private Context context;

    public PlayerStatsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.player_stats_row, parent, false );
        return new MatchLeagueViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MatchLeagueViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }


    public class MatchLeagueViewHolder extends RecyclerView.ViewHolder {

        public MatchLeagueViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}