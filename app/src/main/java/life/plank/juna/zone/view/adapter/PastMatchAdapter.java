package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.LineupActivity;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class PastMatchAdapter extends RecyclerView.Adapter<PastMatchAdapter.MatchFixtureAndResultViewHolder> {
    private Context context;
    private HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap;


    public PastMatchAdapter(Context context, HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap) {
        this.context = context;
        this.classifiedMatchesMap = classifiedMatchesMap;    }

    @Override
    public MatchFixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MatchFixtureAndResultViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.past_match_list, parent, false ) );
    }

    @Override
    public void onBindViewHolder(MatchFixtureAndResultViewHolder holder, int position) {
        Long matchId = classifiedMatchesMap.get( FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES ).get( position ).getForeignId();
        holder.dateSchedule.setText( String.valueOf(classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES  ).get( position ).getMatchStartTime()));
        if (classifiedMatchesMap.get( FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES ).get( position ).getHomeTeam().getLogoLink() != null) {
            Picasso.with( context )
                    .load( classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES  ).get( position ).getHomeTeam().getLogoLink() )
                    .fit().centerCrop()
                    .placeholder( R.drawable.ic_place_holder )
                    .transform( new RoundedTransformation( UIDisplayUtil.dpToPx( 8, context ), 0 ) )
                    .error( R.drawable.ic_place_holder )
                    .into( holder.homeTeamLogo );
        }
        if (classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES ).get( position ).getHomeTeam().getLogoLink() != null) {
            Picasso.with( context )
                    .load( classifiedMatchesMap.get( FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES  ).get( position ).getAwayTeam().getLogoLink() )
                    .fit().centerCrop()
                    .placeholder( R.drawable.ic_place_holder )
                    .transform( new RoundedTransformation( UIDisplayUtil.dpToPx( 8, context ), 0 ) )
                    .error( R.drawable.ic_place_holder )
                    .into( holder.awayTeamLogo );
        }
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, LineupActivity.class );
                intent.putExtra( "MATCH_ID", matchId );
                context.startActivity( intent );
            }
        } );
    }
    @Override
    public int getItemCount() {
        return classifiedMatchesMap.get( FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES  ).size();
    }

    public class MatchFixtureAndResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogo;
        @BindView(R.id.away_team_logo)
        ImageView awayTeamLogo;
        @BindView(R.id.date_schedule)
        TextView dateSchedule;
        public MatchFixtureAndResultViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}