package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.StandingModel;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder> {

    private Picasso picasso;
    private List<StandingModel> standingModelList;

    public StandingTableAdapter(Picasso picasso) {
        this.picasso = picasso;
        this.standingModelList = new ArrayList<>();
    }

    @Override
    public StandingScoreTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_row, parent, false);
        return new StandingScoreTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StandingScoreTableViewHolder holder, int position) {
        holder.serialNumberTextView.setText(String.valueOf(position + 1));
        holder.teamNameTextView.setText(standingModelList.get(position).getTeamName());
        holder.playedTextView.setText(String.valueOf(standingModelList.get(position).getMatchesPlayed()));
        holder.winTextView.setText(String.valueOf(standingModelList.get(position).getWins()));
        holder.drawTextView.setText(String.valueOf(standingModelList.get(position).getDraws()));
        holder.lossTextView.setText(String.valueOf(standingModelList.get(position).getLosses()));
        holder.goalForTextView.setText(String.valueOf(standingModelList.get(position).getGoalsFor()));
        holder.goalAgainstTextView.setText(String.valueOf(standingModelList.get(position).getGoalsAgainst()));
        holder.goalDifferenceTextView.setText(String.valueOf(standingModelList.get(position).getGoalDifference()));
        holder.pointTableTextView.setText(String.valueOf(standingModelList.get(position).getPoints()));
        picasso.load(standingModelList.get(position).getFootballTeamLogo())
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(holder.teamLogoImageView);
    }

    public void update(List<StandingModel> standingModelList) {
        this.standingModelList.addAll(standingModelList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return standingModelList.size();
    }

    public class StandingScoreTableViewHolder extends RecyclerView.ViewHolder {
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