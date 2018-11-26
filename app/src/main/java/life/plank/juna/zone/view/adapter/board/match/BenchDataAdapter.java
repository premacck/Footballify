package life.plank.juna.zone.view.adapter.board.match;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.util.BaseRecyclerView;

public class BenchDataAdapter extends BaseRecyclerView.Adapter<BenchDataAdapter.SubstitutionViewHolder> {

    private List<MatchEvent> matchEventList;

    public BenchDataAdapter() {
        matchEventList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubstitutionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubstitutionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bench_data, parent, false), this);
    }

    @Override
    public int getItemCount() {
        return matchEventList.size();
    }

    public void update(List<MatchEvent> matchEventList) {
        this.matchEventList.addAll(matchEventList);
        notifyDataSetChanged();
    }

    public void updateNew(List<MatchEvent> matchEventList) {
        int previousIndex = this.matchEventList.size() - 1;
        this.matchEventList.addAll(matchEventList);
        notifyItemRangeInserted(previousIndex, matchEventList.size());
    }

    static class SubstitutionViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<BenchDataAdapter> ref;
        @BindView(R.id.minute)
        TextView minuteTextView;
        @BindView(R.id.home_player_name)
        TextView homePlayerName;
        @BindView(R.id.visiting_player_name)
        TextView visitingPlayerName;

        SubstitutionViewHolder(View itemView, BenchDataAdapter benchDataAdapter) {
            super(itemView);
            this.ref = new WeakReference<>(benchDataAdapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            MatchEvent event = ref.get().matchEventList.get(getAdapterPosition());
            if (event.isHomeTeam()) {
                homePlayerName.setText(event.getPlayerName());
                visitingPlayerName.setVisibility(View.GONE);
            } else {
                visitingPlayerName.setText(event.getPlayerName());
                homePlayerName.setVisibility(View.GONE);
            }
            homePlayerName.setCompoundDrawablesWithIntrinsicBounds(event.isHomeTeam() ? R.drawable.ic_substitute_in : 0, 0, 0, 0);
            visitingPlayerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, event.isHomeTeam() ? 0 : R.drawable.ic_substitute_in, 0);
            String timeText;
            timeText = (event.getExtraMinute() > 0 ?
                    event.getMinute() + " + " + event.getExtraMinute() :
                    event.getMinute()) + "'";
            minuteTextView.setText(timeText);
        }
    }
}
