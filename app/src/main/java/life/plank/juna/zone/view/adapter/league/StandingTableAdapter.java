package life.plank.juna.zone.view.adapter.league;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.Standings;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder> {

    private RequestManager glide;
    private boolean isSerialNumberHidden;
    private List<Standings> standingsList;

    public StandingTableAdapter(RequestManager glide, boolean isSerialNumberHidden) {
        this.glide = glide;
        this.isSerialNumberHidden = isSerialNumberHidden;
        this.standingsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public StandingScoreTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_row, parent, false);
        return new StandingScoreTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StandingScoreTableViewHolder holder, int position) {
        if (isSerialNumberHidden) {
            holder.serialNumberTextView.setVisibility(View.GONE);
        } else {
            holder.serialNumberTextView.setVisibility(View.VISIBLE);
            holder.serialNumberTextView.setText(String.valueOf(position + 1));
        }
        holder.teamNameTextView.setText(standingsList.get(position).getTeamName());
        holder.playedTextView.setText(String.valueOf(standingsList.get(position).getMatchesPlayed()));
        holder.winTextView.setText(String.valueOf(standingsList.get(position).getWins()));
        holder.drawTextView.setText(String.valueOf(standingsList.get(position).getDraws()));
        holder.lossTextView.setText(String.valueOf(standingsList.get(position).getLosses()));
        holder.goalForTextView.setText(String.valueOf(standingsList.get(position).getGoalsFor()));
        holder.goalAgainstTextView.setText(String.valueOf(standingsList.get(position).getGoalsAgainst()));
        holder.goalDifferenceTextView.setText(String.valueOf(standingsList.get(position).getGoalDifference()));
        holder.pointTableTextView.setText(String.valueOf(standingsList.get(position).getPoints()));
        glide.load(standingsList.get(position).getFootballTeamLogo())
                .apply(RequestOptions.centerCropTransform()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(holder.teamLogoImageView);
    }

    public void update(List<Standings> standingsList) {
        this.standingsList = standingsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return standingsList.size();
    }

    public List<Standings> getStandings() {
        return standingsList;
    }

    static class StandingScoreTableViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.serial_number_text_view)
        TextView serialNumberTextView;
        @BindView(R.id.team_name_text_view)
        TextView teamNameTextView;
        @BindView(R.id.played_text_view)
        TextView playedTextView;
        @BindView(R.id.win_text_view)
        TextView winTextView;
        @BindView(R.id.draw_text_view)
        TextView drawTextView;
        @BindView(R.id.loss_text_view)
        TextView lossTextView;
        @BindView(R.id.goal_difference_text_view)
        TextView goalDifferenceTextView;
        @BindView(R.id.goal_for_text_view)
        TextView goalForTextView;
        @BindView(R.id.point_table_text_view)
        TextView pointTableTextView;
        @BindView(R.id.team_logo_image_view)
        ImageView teamLogoImageView;
        @BindView(R.id.goal_against_text_view)
        TextView goalAgainstTextView;

        StandingScoreTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}