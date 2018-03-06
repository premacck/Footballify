package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.viewmodel.StandingFeedModel;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder> {
    private Context context;
    private List<StandingFeedModel> standingFeedModelList = new ArrayList<>();

    public StandingTableAdapter(Context context) {
        this.context = context;
        standingFeedModelList.addAll(StandingFeedModel.getStandingData(context));
    }

    @Override
    public StandingScoreTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standing_fragment_row, parent, false);
        return new StandingScoreTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StandingScoreTableViewHolder holder, int position) {
        if (position == 0) {
            holder.teamNameTextView.setText("Team");
            holder.playedTextView.setText("P");
            holder.winTextView.setText("W");
            holder.drawTextView.setText("D");
            holder.lossTextView.setText("L");
            holder.goalDifferenceTextView.setText("GD");
            holder.pointTableTextView.setText("Pts");
            holder.scoreBoardLinerLayout.setBackgroundColor(context.getResources().getColor(R.color.chat_body_background));
            holder.teamNameTextView.setTypeface(holder.teamNameTextView.getTypeface(), Typeface.BOLD);
            holder.playedTextView.setTypeface(holder.playedTextView.getTypeface(), Typeface.BOLD);
            holder.winTextView.setTypeface(holder.winTextView.getTypeface(), Typeface.BOLD);
            holder.drawTextView.setTypeface(holder.drawTextView.getTypeface(), Typeface.BOLD);
            holder.lossTextView.setTypeface(holder.lossTextView.getTypeface(), Typeface.BOLD);
            holder.goalDifferenceTextView.setTypeface(holder.goalDifferenceTextView.getTypeface(), Typeface.BOLD);
            holder.pointTableTextView.setTypeface(holder.pointTableTextView.getTypeface(), Typeface.BOLD);
            holder.serialNumberTextView.setVisibility(View.INVISIBLE);
            holder.scoreView.setVisibility(View.INVISIBLE);
        } else {
            holder.teamNameTextView.setText(standingFeedModelList.get(position).getTeamName());
            holder.playedTextView.setText(standingFeedModelList.get(position).getPlayed());
            holder.winTextView.setText(standingFeedModelList.get(position).getWin());
            holder.drawTextView.setText(standingFeedModelList.get(position).getDraw());
            holder.lossTextView.setText(standingFeedModelList.get(position).getLost());
            holder.goalDifferenceTextView.setText(standingFeedModelList.get(position).getGoalDifference());
            holder.pointTableTextView.setText(standingFeedModelList.get(position).getPointTable());
            holder.scoreBoardLinerLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (position != 0)
            holder.serialNumberTextView.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return standingFeedModelList.size();
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
        @BindView(R.id.point_table_text_view)
        TextView pointTableTextView;
        @BindView(R.id.score_view)
        View scoreView;
        @BindView(R.id.score_board_liner_layout)
        LinearLayout scoreBoardLinerLayout;

        public StandingScoreTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}