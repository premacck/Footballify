package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import life.plank.juna.zone.R;
import life.plank.juna.zone.util.BaseRecyclerView;

public class FixtureDateAdapter extends BaseRecyclerView.Adapter<FixtureDateAdapter.FixtureDateViewHolder> {

    @Override
    public FixtureDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FixtureDateViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture_date, parent, false),
                this
        );
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class FixtureDateViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<FixtureDateAdapter> ref;

        FixtureDateViewHolder(View itemView, FixtureDateAdapter fixtureDateAdapter) {
            super(itemView);
            ref = new WeakReference<>(fixtureDateAdapter);
        }

        @Override
        public void bind() {
        }
    }
}
