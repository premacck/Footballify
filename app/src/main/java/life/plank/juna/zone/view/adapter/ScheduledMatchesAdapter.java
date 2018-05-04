package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.MatchLeagueActivity;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class ScheduledMatchesAdapter extends RecyclerView.Adapter<ScheduledMatchesAdapter.MatchFixtureAndResultViewHolder> {
    private Context context;

    public ScheduledMatchesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchFixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchFixtureAndResultViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.upcoming_match_list, parent, false ) );

    }

    @Override
    public void onBindViewHolder(MatchFixtureAndResultViewHolder holder, int position) {
        Intent intent = new Intent( context, MatchLeagueActivity.class );
        context.startActivity( intent );
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MatchFixtureAndResultViewHolder extends RecyclerView.ViewHolder {
        public MatchFixtureAndResultViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}