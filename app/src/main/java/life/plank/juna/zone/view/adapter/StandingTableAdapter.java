package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingScoreTableViewHolder> {
    private Context context;

    public StandingTableAdapter(Context context) {
        this.context = context;
    }

    @Override
    public StandingScoreTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standing_row, parent, false);
        return new StandingScoreTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StandingScoreTableViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
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