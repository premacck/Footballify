package life.plank.juna.zone.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FixtureByMatchDay;
import life.plank.juna.zone.interfaces.LeagueContainer;
import life.plank.juna.zone.util.BaseRecyclerView;

import static life.plank.juna.zone.util.AppConstants.TODAY_MATCHES;
import static life.plank.juna.zone.util.DataUtil.findString;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.findColor;
import static life.plank.juna.zone.view.fragment.football.LeagueInfoFragment.fixtureByMatchDayList;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class FixtureMatchdayAdapter extends BaseRecyclerView.Adapter<FixtureMatchdayAdapter.FixtureMatchDayViewHolder> {

    private LeagueContainer leagueContainer;

    public FixtureMatchdayAdapter(LeagueContainer leagueContainer) {
        this.leagueContainer = leagueContainer;
    }

    @NonNull
    @Override
    public FixtureMatchDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureMatchDayViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_matchday, parent, false),
                this
        );
    }

    @Override
    public int getItemCount() {
        return isNullOrEmpty(fixtureByMatchDayList) ? 0 : fixtureByMatchDayList.size();
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
            FixtureByMatchDay fixtureByMatchDay = fixtureByMatchDayList.get(getAdapterPosition());

            if (!isNullOrEmpty(fixtureByMatchDay.getFixtureByDateList())) {
                matchdayHeader.setBackgroundResource(
                        fixtureByMatchDay.getDaySection().equals(TODAY_MATCHES) ?
                                R.color.light_header :
                                R.color.lighter_header
                );
                matchdayHeader.setTextColor(findColor(
                        fixtureByMatchDay.getDaySection().equals(TODAY_MATCHES) ?
                                R.color.fab_button_pink :
                                R.color.grey
                ));
                String matchdayHeaderText =
                        findString(ref.get().leagueContainer.getTheLeague().isCup() ? R.string.round_ : R.string.matchday_) +
                                (ref.get().leagueContainer.getTheLeague().isCup() ? fixtureByMatchDay.getMatchDay() : fixtureByMatchDay.getMatchDay());
                matchdayHeader.setText(matchdayHeaderText);
                recyclerView.setAdapter(new FixtureDateAdapter(fixtureByMatchDay.getFixtureByDateList(), ref.get().leagueContainer));
            }
        }
    }
}