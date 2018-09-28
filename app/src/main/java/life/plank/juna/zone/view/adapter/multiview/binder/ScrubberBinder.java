package life.plank.juna.zone.view.adapter.multiview.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.github.mikephil.charting.charts.LineChart;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.ScrubberData;
import life.plank.juna.zone.util.DataUtil.ScrubberLoader;
import life.plank.juna.zone.view.adapter.multiview.BoardInfoAdapter;
import lombok.Data;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class ScrubberBinder extends ItemBinder<ScrubberBinder.ScrubberBindingModel, ScrubberBinder.ScrubberViewHolder> {

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
        holder.progressBar.setVisibility(View.GONE);
        ScrubberLoader.prepare(holder.scrubber, false);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ScrubberBindingModel;
    }

    static class ScrubberViewHolder extends ItemViewHolder<ScrubberBindingModel> {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.scrubber)
        LineChart scrubber;
        @BindView(R.id.scrubber_expand_collapse)
        ImageButton scrubberExpandCollapse;

        private final WeakReference<ScrubberBinder> ref;
        private boolean isExpanded;

        ScrubberViewHolder(View itemView, ScrubberBinder scrubberBinder) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(scrubberBinder);
        }

        @OnClick(R.id.scrubber)
        public void onScrubberClick() {
            ref.get().listener.onScrubberClick(scrubber);
        }

        @OnClick(R.id.scrubber_expand_collapse)
        public void onScrubberExpandCollapseClick() {
            isExpanded = !isExpanded;
            scrubberExpandCollapse.setImageResource(isExpanded ?
                    R.drawable.ic_collapse :
                    R.drawable.ic_expand
            );
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrubber.getLayoutParams();
            params.height = (int) getDp(isExpanded ? 250 : 70);
            scrubber.setLayoutParams(params);
        }

    }

    @Data
    public static class ScrubberBindingModel {
        private final List<ScrubberData> scrubberDataList;

        public static ScrubberBindingModel from(MatchDetails matchDetails) {
            return new ScrubberBindingModel(matchDetails.getScrubberDataList());
        }
    }
}