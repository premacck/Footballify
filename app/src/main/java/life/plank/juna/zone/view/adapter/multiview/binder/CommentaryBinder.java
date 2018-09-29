package life.plank.juna.zone.view.adapter.multiview.binder;

import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.view.adapter.CommentaryAdapter;
import life.plank.juna.zone.view.adapter.multiview.BoardInfoAdapter;
import lombok.Data;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class CommentaryBinder extends ItemBinder<CommentaryBinder.CommentaryBindingModel, CommentaryBinder.CommentaryViewHolder> {

    private BoardInfoAdapter.BoardInfoAdapterListener listener;

    public CommentaryBinder(BoardInfoAdapter.BoardInfoAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public CommentaryViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new CommentaryViewHolder(inflater.inflate(R.layout.item_live_commentary_small, parent, false), this);
    }

    @Override
    public void bind(CommentaryViewHolder holder, CommentaryBindingModel item) {
        holder.progressBar.setVisibility(View.GONE);
        if (isNullOrEmpty(item.getCommentaryList()) || item.getErrorMessage() != null) {
            holder.commentaryRecyclerView.setVisibility(View.GONE);
            holder.seeAllBtn.setVisibility(View.GONE);
            holder.noDataTextView.setVisibility(View.VISIBLE);
            holder.noDataTextView.setText(item.getErrorMessage());
            return;
        }

        ((LinearLayoutManager) holder.commentaryRecyclerView.getLayoutManager()).setReverseLayout(true);
        holder.commentaryRecyclerView.scrollToPosition(item.getCommentaryList().size() - 1);
        holder.commentaryRecyclerView.setVisibility(View.VISIBLE);
        holder.seeAllBtn.setVisibility(View.VISIBLE);
        holder.noDataTextView.setVisibility(View.GONE);
        holder.commentaryRecyclerView.setAdapter(new CommentaryAdapter(item.getCommentaryList()));
    }

    @Override
    public void bind(CommentaryViewHolder holder, CommentaryBindingModel item, List payloads) {
        super.bind(holder, item, payloads);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof CommentaryBindingModel;
    }

    static class CommentaryViewHolder extends ItemViewHolder<CommentaryBindingModel> {

        @BindView(R.id.see_all)
        Button seeAllBtn;
        @BindView(R.id.commentary_list)
        RecyclerView commentaryRecyclerView;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.no_data)
        TextView noDataTextView;

        private final WeakReference<CommentaryBinder> ref;

        CommentaryViewHolder(View itemView, CommentaryBinder commentaryBinder) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(commentaryBinder);
        }

        @OnClick(R.id.see_all)
        public void onCommentarySeeAllClick() {
            ref.get().listener.onCommentarySeeAllClick(itemView);
        }
    }

    @Data
    public static class CommentaryBindingModel {
        private final List<Commentary> commentaryList;
        @StringRes
        private final Integer errorMessage;

        public static CommentaryBindingModel from(MatchDetails matchDetails) {
            return new CommentaryBindingModel(
                    matchDetails.getCommentary(),
                    isNullOrEmpty(matchDetails.getCommentary()) ? R.string.commentaries_not_available : null
            );
        }
    }
}
