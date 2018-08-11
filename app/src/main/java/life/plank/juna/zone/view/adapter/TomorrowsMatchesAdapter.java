package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.LineupActivity;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class TomorrowsMatchesAdapter extends RecyclerView.Adapter<TomorrowsMatchesAdapter.MatchFixtureAndResultViewHolder> {
    private Context context;

    public TomorrowsMatchesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchFixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MatchFixtureAndResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_fixture, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchFixtureAndResultViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LineupActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class MatchFixtureAndResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_team_logo) ImageView homeTeamLogo;
        @BindView(R.id.away_team_logo) ImageView awayTeamLogo;
        @BindView(R.id.date_schedule) TextView dateSchedule;
        @BindView(R.id.team_names) TextView teamNames;

        MatchFixtureAndResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}