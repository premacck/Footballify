package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.view.activity.FixtureActivity;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateHeader;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class FixtureMatchdayAdapter extends BaseRecyclerView.Adapter<FixtureMatchdayAdapter.FixtureMatchDayViewHolder> {

    private FixtureActivity activity;
    private List<SectionedFixture> sectionedFixtureList;
    private Picasso picasso;

    public FixtureMatchdayAdapter(FixtureActivity activity, Picasso picasso) {
        this.activity = activity;
        this.picasso = picasso;
        this.sectionedFixtureList = new ArrayList<>();
    }

    @Override
    public FixtureMatchDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureMatchDayViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_matchday, parent, false),
                this
        );
    }

    public void update(List<SectionedFixture> sectionedFixtureList) {
        this.sectionedFixtureList.addAll(sectionedFixtureList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sectionedFixtureList.size();
    }

    public static class FixtureMatchDayViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<FixtureMatchdayAdapter> ref;
        @BindView(R.id.matchday_header)
        TextView matchdayHeader;
        @BindView(R.id.date_time)
        TextView dateTime;
        @BindView(R.id.fixtures_list)
        RecyclerView recyclerView;
        private SectionedFixture sectionedFixture;

        FixtureMatchDayViewHolder(View itemView, FixtureMatchdayAdapter adapter) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            sectionedFixture = ref.get().sectionedFixtureList.get(getAdapterPosition());

            if (!isNullOrEmpty(sectionedFixture.getScoreFixtureList())) {
                String matchdayHeaderText = ref.get().activity.getString(R.string.matchday_) + sectionedFixture.getMatchday();
                matchdayHeader.setText(matchdayHeaderText);
                dateTime.setText(getDateHeader(
                        ref.get().activity,
                        sectionedFixture.getSection(),
                        sectionedFixture.getScoreFixtureList().get(0).getMatchStartTime()
                ));
                recyclerView.setAdapter(
                        new FixtureAdapter(
                                sectionedFixture.getScoreFixtureList(),
                                ref.get().picasso,
                                ref.get().activity,
                                sectionedFixture.getSection()));
            }
        }
    }
}