package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.TeamStateViewHolder> {
    private Context context;

    public TeamStatsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TeamStateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.team_stats_row, parent, false );
        return new TeamStateViewHolder( view );
    }

    @Override
    public void onBindViewHolder(TeamStateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class TeamStateViewHolder extends RecyclerView.ViewHolder {

        public TeamStateViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}