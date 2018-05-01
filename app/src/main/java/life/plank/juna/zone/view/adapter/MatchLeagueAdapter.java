package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class MatchLeagueAdapter extends RecyclerView.Adapter<MatchLeagueAdapter.MatchLeagueViewHolder> {
    private Context context;

    public MatchLeagueAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.match_league_row, parent, false );
        return new MatchLeagueViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MatchLeagueViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }


    public class MatchLeagueViewHolder extends RecyclerView.ViewHolder {

        public MatchLeagueViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}