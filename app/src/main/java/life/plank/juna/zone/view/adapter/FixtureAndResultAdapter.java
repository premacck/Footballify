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

    @Override public FixtureAndResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureAndResultViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_fixture_section, parent, false),
                this
        );
    }

    public void update(List<SectionedFixture> sectionedFixtureList) {
        this.sectionedFixtureList.addAll(sectionedFixtureList);
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        return sectionedFixtureList.size();
    }

    public static class FixtureAndResultViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.section_header) TextView sectionHeader;
        @BindView(R.id.fixtures_list) RecyclerView pastMatchRecyclerView;

        private final WeakReference<FixtureAndResultAdapter> ref;
        private SectionedFixture sectionedFixture;

        FixtureAndResultViewHolder(View itemView, FixtureAndResultAdapter adapter) {
            super(itemView);
            this.ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override public void bind() {
            sectionedFixture = ref.get().sectionedFixtureList.get(getAdapterPosition());

            sectionHeader.setText(getCurrentSectionHeader());
            pastMatchRecyclerView.setAdapter(
                    new FixtureAndResultSectionAdapter(
                            sectionedFixture.getScoreFixtureModelList(),
                            ref.get().picasso,
                            ref.get().context,
                            sectionedFixture.getClassification()));
        }

        private String getCurrentSectionHeader() {
            switch (sectionedFixture.getClassification()) {
                case PAST_MATCHES:
                    return "Past Matches";
                case LIVE_MATCHES:
                case TOMORROWS_MATCHES:
                    return "Matchday " + String.valueOf(sectionedFixture.getScoreFixtureModelList().get(0).getMatchDay());
                case SCHEDULED_MATCHES:
                    return "Scheduled matches";
                default:
                    return "Fixtures";
            }
        }
    }
}