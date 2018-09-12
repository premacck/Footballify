package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
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
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.view.activity.FixtureActivity;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class FixtureMatchdayAdapter extends BaseRecyclerView.Adapter<FixtureMatchdayAdapter.FixtureMatchDayViewHolder> {

    private FixtureActivity activity;
    private List<FixtureByMatchDay> fixtureByMatchDayList;

    public FixtureMatchdayAdapter(FixtureActivity activity) {
        this.activity = activity;
        this.fixtureByMatchDayList = new ArrayList<>();
    }

    @Override
    public FixtureMatchDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureMatchDayViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_matchday, parent, false),
                this
        );
    }

    public void update(List<FixtureByMatchDay> fixtureByMatchDayList) {
        this.fixtureByMatchDayList.addAll(fixtureByMatchDayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fixtureByMatchDayList.size();
    }

    public static class FixtureMatchDayViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<FixtureMatchdayAdapter> ref;
        @BindView(R.id.matchday_header)
        TextView matchdayHeader;
        @BindView(R.id.fixtures_matchday_list)
        RecyclerView recyclerView;

        FixtureMatchDayViewHolder(View itemView, FixtureMatchdayAdapter adapter) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            FixtureByMatchDay fixtureByMatchDay = ref.get().fixtureByMatchDayList.get(getAdapterPosition());

            if (!isNullOrEmpty(fixtureByMatchDay.getFixtureByDateList())) {
                String matchdayHeaderText =
                        ref.get().activity.getString(ref.get().activity.isCup ? R.string.matchday_ : R.string.round_) +
                                (ref.get().activity.isCup ? fixtureByMatchDay.getMatchDay() : (fixtureByMatchDay.getMatchDay() + 1));
                matchdayHeader.setText(matchdayHeaderText);
                recyclerView.setAdapter(new FixtureDateAdapter(fixtureByMatchDay.getFixtureByDateList(), ref.get().activity));
            }
        }
    }
}