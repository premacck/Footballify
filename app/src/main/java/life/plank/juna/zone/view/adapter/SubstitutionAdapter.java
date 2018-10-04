package life.plank.juna.zone.view.adapter;

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

public class SubstitutionAdapter extends BaseRecyclerView.Adapter<SubstitutionAdapter.SubstitutionViewHolder> {

    private List<MatchEvent> matchEventList;

    public SubstitutionAdapter() {
        matchEventList = new ArrayList<>();
    }

    @Override
    public SubstitutionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubstitutionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_substitutes, parent, false), this);
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

        @BindView(R.id.minute)
        TextView minuteTextView;
        @BindView(R.id.home_player_name)
        TextView homePlayerName;
        @BindView(R.id.home_related_player_name)
        TextView homeRelatedPlayerName;
        @BindView(R.id.visiting_player_name)
        TextView visitingPlayerName;
        @BindView(R.id.visiting_related_player_name)
        TextView visitingRelatedPlayerName;

        private final WeakReference<SubstitutionAdapter> ref;

        SubstitutionViewHolder(View itemView, SubstitutionAdapter substitutionAdapter) {
            super(itemView);
            this.ref = new WeakReference<>(substitutionAdapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            MatchEvent event = ref.get().matchEventList.get(getAdapterPosition());
            if (event.isHomeTeam()) {
                homePlayerName.setText(event.getPlayerName());
                homeRelatedPlayerName.setText(event.getRelatedPlayerName());
                visitingPlayerName.setVisibility(View.GONE);
                visitingRelatedPlayerName.setVisibility(View.GONE);
            } else {
                visitingPlayerName.setText(event.getPlayerName());
                visitingRelatedPlayerName.setText(event.getRelatedPlayerName());
                homePlayerName.setVisibility(View.GONE);
                homeRelatedPlayerName.setVisibility(View.GONE);
            }
            homePlayerName.setCompoundDrawablesWithIntrinsicBounds(event.isHomeTeam() ? R.drawable.ic_substitute_in : 0, 0, 0, 0);
            homeRelatedPlayerName.setCompoundDrawablesWithIntrinsicBounds(event.isHomeTeam() ? R.drawable.ic_substitute_out : 0, 0, 0, 0);
            visitingPlayerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, event.isHomeTeam() ? 0 : R.drawable.ic_substitute_in, 0);
            visitingRelatedPlayerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, event.isHomeTeam() ? 0 : R.drawable.ic_substitute_out, 0);
            String timeText;
            timeText = (event.getExtraMinute() > 0 ?
                    event.getMinute() + " + " + event.getExtraMinute() :
                    event.getMinute()) + "'";
            minuteTextView.setText(timeText);
        }
    }
}
