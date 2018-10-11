package life.plank.juna.zone.view.adapter.multiview.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.github.mikephil.charting.charts.LineChart;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.binder.ScrubberBindingModel;
import life.plank.juna.zone.util.DataUtil.ScrubberLoader;
import life.plank.juna.zone.view.adapter.multiview.BoardInfoAdapter;

public class ScrubberBinder extends ItemBinder<ScrubberBindingModel, ScrubberBinder.ScrubberViewHolder> {

    private BoardInfoAdapter.BoardInfoAdapterListener listener;

    public ScrubberBinder(BoardInfoAdapter.BoardInfoAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ScrubberViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ScrubberViewHolder(inflater.inflate(R.layout.item_scrubber, parent, false), this);
    }

    @Override
    public void bind(ScrubberViewHolder holder, ScrubberBindingModel item) {
        ScrubberLoader.prepare(holder.scrubber, false);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ScrubberBindingModel;
    }

    static class ScrubberViewHolder extends ItemViewHolder<ScrubberBindingModel> {

        @BindView(R.id.scrubber)
        LineChart scrubber;

        private final WeakReference<ScrubberBinder> ref;

        ScrubberViewHolder(View itemView, ScrubberBinder scrubberBinder) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(scrubberBinder);
        }

        @OnClick(R.id.scrubber)
        public void onScrubberClick() {
            ref.get().listener.onScrubberClick(scrubber);
        }
    }
}