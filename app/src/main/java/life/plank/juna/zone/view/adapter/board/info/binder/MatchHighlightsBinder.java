package life.plank.juna.zone.view.adapter.board.info.binder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.intik.overflowindicator.OverflowPagerIndicator;
import cz.intik.overflowindicator.SimpleSnapHelper;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel;
import life.plank.juna.zone.util.customview.HighlightsAdapter;

import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;

public class MatchHighlightsBinder extends ItemBinder<HighlightsBindingModel, MatchHighlightsBinder.MatchHighlightsViewHolder> {

    private Activity activity;
    private SimpleSnapHelper snapHelper;

    public MatchHighlightsBinder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public MatchHighlightsViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new MatchHighlightsViewHolder(inflater.inflate(R.layout.item_match_highlights_layout, parent, false));
    }


    @Override
    public void bind(MatchHighlightsViewHolder holder, HighlightsBindingModel item) {
        holder.progressBar.setVisibility(View.GONE);
        int highlightsWidth = getScreenSize(activity.getWindowManager().getDefaultDisplay())[0];
        int highlightsHeight = (highlightsWidth * 9) / 17;

        if (snapHelper == null) {
            snapHelper = new SimpleSnapHelper(holder.overflowPagerIndicator);
            snapHelper.attachToRecyclerView(holder.highlightsRecyclerView);
        }
        HighlightsAdapter adapter = new HighlightsAdapter(highlightsHeight);
        holder.highlightsRecyclerView.setAdapter(adapter);
        holder.overflowPagerIndicator.attachToRecyclerView(holder.highlightsRecyclerView);
        adapter.update(item.getHighlightsList());
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof HighlightsBindingModel;
    }

    static class MatchHighlightsViewHolder extends ItemViewHolder<HighlightsBindingModel> {

        @BindView(R.id.list_highlights)
        RecyclerView highlightsRecyclerView;
        @BindView(R.id.overflow_pager_indicator)
        OverflowPagerIndicator overflowPagerIndicator;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        MatchHighlightsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
