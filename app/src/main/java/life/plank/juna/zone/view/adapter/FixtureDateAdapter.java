package life.plank.juna.zone.view.adapter;

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
import life.plank.juna.zone.data.network.model.SectionedFixtureDate;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.view.activity.FixtureActivity;

public class FixtureDateAdapter extends BaseRecyclerView.Adapter<FixtureDateAdapter.FixtureDateViewHolder> {

    private List<SectionedFixtureDate> sectionedFixtureDateList;
    private FixtureActivity activity;

    FixtureDateAdapter(List<SectionedFixtureDate> sectionedFixtureDateList, FixtureActivity activity) {
        this.sectionedFixtureDateList = sectionedFixtureDateList;
        this.activity = activity;
    }

    @Override
    public FixtureDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureDateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_date, parent, false), this);
    }

    @Override
    public int getItemCount() {
        return sectionedFixtureDateList.size();
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
            SectionedFixtureDate sectionedFixtureDate = ref.get().sectionedFixtureDateList.get(getAdapterPosition());

            dateTime.setText(sectionedFixtureDate.getDate());
            recyclerView.setAdapter(
                    new FixtureAdapter(
                            sectionedFixtureDate.getScoreFixtureList(),
                            ref.get().activity,
                            sectionedFixtureDate.getSection()
                    )
            );
        }
    }
}
