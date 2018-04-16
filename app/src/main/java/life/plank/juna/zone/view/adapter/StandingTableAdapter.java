package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.StandingModel;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder> {
    private Context context;
    private List<StandingModel> standingModelList;

    public StandingTableAdapter(Context context, List<StandingModel> standingModelList) {
        this.context = context;
        this.standingModelList = standingModelList;
    }

    @Override
    public StandingScoreTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standing_fragment_row, parent, false);
        return new StandingScoreTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StandingScoreTableViewHolder holder, int position) {
        if (standingModelList.get(position) == null) {
            holder.teamNameTextView.setText("Team");
            holder.playedTextView.setText("P");
            holder.winTextView.setText("W");
            holder.drawTextView.setText("D");
            holder.lossTextView.setText("L");
            holder.goalDifferenceTextView.setText("GD");
            holder.pointTableTextView.setText("Pts");
            holder.scoreBoardLinerLayout.setBackgroundColor(context.getResources().getColor(R.color.chat_body_background));
            holder.playedTextView.setTypeface(holder.playedTextView.getTypeface(), Typeface.BOLD);
            holder.winTextView.setTypeface(holder.winTextView.getTypeface(), Typeface.BOLD);
            holder.drawTextView.setTypeface(holder.drawTextView.getTypeface(), Typeface.BOLD);
            holder.lossTextView.setTypeface(holder.lossTextView.getTypeface(), Typeface.BOLD);
            holder.goalDifferenceTextView.setTypeface(holder.goalDifferenceTextView.getTypeface(), Typeface.BOLD);
            holder.scoreView.setVisibility(View.INVISIBLE);
            holder.serialNumberTextView.setText("");
        } else {
            holder.teamNameTextView.setText(standingModelList.get(position).getFootballTeam());
            holder.playedTextView.setText(String.valueOf(standingModelList.get(position).getGamesPlayed()));
            holder.winTextView.setText(String.valueOf(standingModelList.get(position).getGamesWon()));
            holder.drawTextView.setText(String.valueOf(standingModelList.get(position).getGamesDrawn()));
            holder.lossTextView.setText(String.valueOf(standingModelList.get(position).getGamesLost()));
            holder.goalDifferenceTextView.setText(String.valueOf(standingModelList.get(position).getGoalsAgainst()));
            holder.pointTableTextView.setText(String.valueOf(standingModelList.get(position).getPoints()));
            holder.scoreBoardLinerLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.serialNumberTextView.setText(String.valueOf(standingModelList.get(position).getPosition()));
            holder.playedTextView.setTypeface(null, Typeface.NORMAL);
            holder.winTextView.setTypeface(null, Typeface.NORMAL);
            holder.drawTextView.setTypeface(null, Typeface.NORMAL);
            holder.lossTextView.setTypeface(null, Typeface.NORMAL);
            holder.goalDifferenceTextView.setTypeface(null, Typeface.NORMAL);
            holder.scoreView.setVisibility(View.VISIBLE);
        }
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