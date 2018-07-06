package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class ViewAllCommentListAdapter extends RecyclerView.Adapter<ViewAllCommentListAdapter.ViewAllCommentListAdapterViewHolder> {

    private Context context;

    public ViewAllCommentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewAllCommentListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_feed_detail_row, parent, false);
        return new ViewAllCommentListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAllCommentListAdapterViewHolder holder, int position) {
    }

    //todo:list size replace with api Call innext pull requets
    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewAllCommentListAdapterViewHolder extends RecyclerView.ViewHolder {
        ViewAllCommentListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
