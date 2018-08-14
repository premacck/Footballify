package life.plank.juna.zone.view.adapter;

import android.content.Context;
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

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.removeEmptySections;

/**
 * Created by plank-prachi on 4/10/2018.
 */
public class FixtureAndResultAdapter extends BaseRecyclerView.Adapter<FixtureAndResultAdapter.FixtureAndResultViewHolder> {

    private Context context;
    private List<SectionedFixture> sectionedFixtureList;
    private Picasso picasso;

    public FixtureAndResultAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        this.sectionedFixtureList = new ArrayList<>();
    }

    @Override
    public FixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureAndResultViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_fixture_section, parent, false),
                context,
                this
        );
    }

    public void update(List<SectionedFixture> sectionedFixtureList) {
        removeEmptySections(sectionedFixtureList);
        this.sectionedFixtureList.addAll(sectionedFixtureList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sectionedFixtureList.size();
    }

    public static class FixtureAndResultViewHolder extends BaseRecyclerView.ViewHolder {

        private final Context context;
        private final WeakReference<FixtureAndResultAdapter> ref;
        @BindView(R.id.section_header)
        TextView sectionHeaderView;
        @BindView(R.id.fixtures_list)
        RecyclerView recyclerView;
        private SectionedFixture sectionedFixture;

        FixtureAndResultViewHolder(View itemView, Context context, FixtureAndResultAdapter adapter) {
            super(itemView);
            this.context = context;
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            sectionedFixture = ref.get().sectionedFixtureList.get(getAdapterPosition());

            if (!isNullOrEmpty(sectionedFixture.getScoreFixtureModelList())) {
                sectionHeaderView.setText(getCurrentSectionHeader());
                recyclerView.setAdapter(
                        new FixtureAndResultSectionAdapter(
                                sectionedFixture.getScoreFixtureModelList(),
                                ref.get().picasso,
                                ref.get().context,
                                sectionedFixture.getSection()));
            }
        }

        private String getCurrentSectionHeader() {
            switch (sectionedFixture.getSection()) {
                case PAST_MATCHES:
                    return context.getString(R.string.past_matches);
                case LIVE_MATCHES:
                    if (!isNullOrEmpty(sectionedFixture.getScoreFixtureModelList())) {
                        return context.getString(R.string.matchday_) + String.valueOf(sectionedFixture.getScoreFixtureModelList().get(0).getMatchDay());
                    } else {
                        return context.getString(R.string.today);
                    }
                case TOMORROWS_MATCHES:
                    if (!isNullOrEmpty(sectionedFixture.getScoreFixtureModelList())) {
                        return context.getString(R.string.matchday_) + String.valueOf(sectionedFixture.getScoreFixtureModelList().get(0).getMatchDay());
                    } else {
                        return context.getString(R.string.tomorrow);
                    }
                case SCHEDULED_MATCHES:
                    return context.getString(R.string.scheduled_matches);
                default:
                    return context.getString(R.string.fixtures);
            }
        }
    }
}