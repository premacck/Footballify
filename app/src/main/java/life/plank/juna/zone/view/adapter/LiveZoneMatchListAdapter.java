package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.LiveZoneMatchListData;

/**
 * Created by plank-niraj on 16-02-2018.
 */

public class LiveZoneMatchListAdapter extends RecyclerView.Adapter<LiveZoneMatchListAdapter.LiveZoneListViewHolder> {

    OnClickListeners onClickListeners;
    ArrayList<LiveZoneMatchListData> matchList;
    private String teamList[];
    private Context context;

    public LiveZoneMatchListAdapter(Context context, OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
        this.context = context;
        matchList = new ArrayList<>();
        setData();
    }

    private void setData() {
        teamList = context.getResources().getStringArray(R.array.football_teams);
        for (int loop = 0; loop < 4; loop++) {
            LiveZoneMatchListData liveZoneMatchListData = new LiveZoneMatchListData(teamList[loop + 2],
                    teamList[loop + 1],
                    loop + 1,
                    loop,
                    context.getResources().getString(R.string.match_commentary)
            );
            matchList.add(liveZoneMatchListData);
        }
    }

    @Override
    public LiveZoneListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_live_zone_single_match,
                parent,
                false);
        return new LiveZoneListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveZoneListViewHolder holder, int position) {
        holder.homeTeamTextView.setText(matchList.get(position).getHomeTeam());
        holder.commentaryTextView.setText(matchList.get(position).getCommentary());
        holder.visitingTeamTextView.setText(matchList.get(position).getVisitingTeam());
        holder.itemView.setOnClickListener(view -> onClickListeners.onClick(position));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public interface OnClickListeners {
        void onClick(int position);
    }

    class LiveZoneListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.visiting_team_goal_text_view)
        TextView visitingTeamScoreTextView;
        @BindView(R.id.home_team_goal_text_view)
        TextView homeTeamScoreTextView;
        @BindView(R.id.home_team_text_view)
        TextView homeTeamTextView;
        @BindView(R.id.visiting_team_text_view)
        TextView visitingTeamTextView;
        @BindView(R.id.commentary_text_view)
        TextView commentaryTextView;

        LiveZoneListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
