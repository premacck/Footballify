package life.plank.juna.zone.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.util.TeamNameMap;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by plank-sobia on 10/5/2017.
 */

public class PointsGameAdapter extends RecyclerView.Adapter<PointsGameAdapter.ViewHolder> {

    private List<FootballMatch> footballMatchList = new ArrayList<>();
    private PublishSubject<FootballMatch> itemViewClickSubject = PublishSubject.create();
    Typeface moderneSansFont = Typeface.createFromAsset(ZoneApplication.getContext().getAssets(),
            ZoneApplication.getContext().getString(R.string.moderne_sans));

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_team_name)
        TextView homeTeamName;
        @BindView(R.id.visiting_team_name)
        TextView visitingTeamName;
        @BindView(R.id.home_team_image)
        ImageView homeTeamImage;
        @BindView(R.id.visiting_team_image)
        ImageView visitingTeamImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            homeTeamName.setTypeface(moderneSansFont);
            visitingTeamName.setTypeface(moderneSansFont);
        }
    }

    public Observable<FootballMatch> getViewClickedObservable() {
        return itemViewClickSubject.asObservable();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points_game_rows, parent, false);
        TeamNameMap.HashMaps(parent.getContext());

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FootballMatch footballMatch = footballMatchList.get(position);

        if (ZoneApplication.selectedTeamsList.contains(footballMatch.getHomeTeam().getName())
                && ZoneApplication.selectedTeamsList.contains(footballMatch.getVisitingTeam().getName())) {
            holder.itemView.setEnabled(false);
            holder.itemView.setAlpha(0.2f);
        } else {
            if (ZoneApplication.selectedTeamsList.contains(footballMatch.getHomeTeam().getName())) {
                holder.homeTeamName.setAlpha(0.2f);
                holder.homeTeamImage.setAlpha(0.2f);
            }
            if (ZoneApplication.selectedTeamsList.contains(footballMatch.getVisitingTeam().getName())) {
                holder.visitingTeamName.setAlpha(0.2f);
                holder.visitingTeamImage.setAlpha(0.2f);
            }
        }
        holder.homeTeamName.setText(footballMatch.getHomeTeam().getName());
        holder.homeTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getHomeTeam().getName()));
        holder.visitingTeamName.setText(footballMatch.getVisitingTeam().getName());
        holder.visitingTeamImage.setImageDrawable(TeamNameMap.getTeamNameMap().get(footballMatch.getVisitingTeam().getName()));
        RxView.clicks(holder.itemView)
                .subscribe(v -> itemViewClickSubject.onNext(footballMatch));
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
