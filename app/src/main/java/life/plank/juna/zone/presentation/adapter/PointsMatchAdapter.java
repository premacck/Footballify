package life.plank.juna.zone.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballMatch;

/**
 * Created by plank-sobia on 10/5/2017.
 */

public class PointsMatchAdapter extends RecyclerView.Adapter<PointsMatchAdapter.ViewHolder> {

    List<FootballMatch> footballMatchList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_team_name)
        TextView homeTeamName;
        @BindView(R.id.visiting_team_name)
        TextView visitingTeamName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_points_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FootballMatch footballMatch = footballMatchList.get(position);
        holder.homeTeamName.setText(footballMatch.getHomeTeam().getName());
        holder.visitingTeamName.setText(footballMatch.getVisitingTeam().getName());
    }


    @Override
    public int getItemCount() {
        return footballMatchList.size();
    }

    public void setFootballMatchList(List<FootballMatch> footballMatches) {
        if (footballMatches == null) {
            return;
        }
        footballMatchList.clear();
        footballMatchList.addAll(footballMatches);
        notifyDataSetChanged();
    }
}
