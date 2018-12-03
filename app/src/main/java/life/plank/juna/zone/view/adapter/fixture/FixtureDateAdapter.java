package life.plank.juna.zone.view.adapter.fixture;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FixtureByDate;
import life.plank.juna.zone.interfaces.LeagueContainer;
import life.plank.juna.zone.util.BaseRecyclerView;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateHeader;

public class FixtureDateAdapter extends BaseRecyclerView.Adapter<FixtureDateAdapter.FixtureDateViewHolder> {

    private List<FixtureByDate> fixtureByDateList;
    private LeagueContainer leagueContainer;

    FixtureDateAdapter(List<FixtureByDate> fixtureByDateList, LeagueContainer leagueContainer) {
        this.fixtureByDateList = fixtureByDateList;
        this.leagueContainer = leagueContainer;
    }

    @NonNull
    @Override
    public FixtureDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureDateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_date, parent, false), this);
    }

    @Override
    public int getItemCount() {
        return fixtureByDateList.size();
    }

    static class FixtureDateViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.date_time)
        TextView dateTime;
        @BindView(R.id.fixtures_date_list)
        RecyclerView recyclerView;

        private final WeakReference<FixtureDateAdapter> ref;

        FixtureDateViewHolder(View itemView, FixtureDateAdapter fixtureDateAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(fixtureDateAdapter);
        }

        @Override
        public void bind() {
            FixtureByDate fixtureByDate = ref.get().fixtureByDateList.get(getAdapterPosition());
            dateTime.setText(
                    !isNullOrEmpty(fixtureByDate.getFixtures()) ?
                            getDateHeader(fixtureByDate.getFixtures().get(0).getMatchStartTime()) :
                            getDateHeader(fixtureByDate.getDate())
            );
            recyclerView.setAdapter(new FixtureAdapter(fixtureByDate.getFixtures(), ref.get().leagueContainer));
        }
    }
}
