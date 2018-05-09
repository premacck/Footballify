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
public class LiveMatchesAdapter extends RecyclerView.Adapter<LiveMatchesAdapter.MatchFixtureAndResultViewHolder> {
    private Context context;

    public LiveMatchesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchFixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MatchFixtureAndResultViewHolder( LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.current_match_details, parent, false ) );

    }

    @Override
    public void onBindViewHolder(MatchFixtureAndResultViewHolder holder, int position) {
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, MatchLeagueActivity.class );
                context.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MatchFixtureAndResultViewHolder extends RecyclerView.ViewHolder {


        public MatchFixtureAndResultViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}