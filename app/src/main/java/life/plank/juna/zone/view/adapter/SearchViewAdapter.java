package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    ArrayList<String> itemsCopy = new ArrayList<>();
    private List<Integer> mViewColors;
    private List<String> mNames;

    public SearchViewAdapter(List<Integer> colors, List<String> names) {
        this.mViewColors = colors;
        this.mNames = names;
        itemsCopy.addAll(mNames);
    }

    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.SearchViewHolder holder, int position) {
        int color = mViewColors.get(position);
        String animal = mNames.get(position);
        holder.tileImageView.setBackgroundColor(color);
        holder.nameTextView.setText(animal);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public void filter(String text) {
        mNames.clear();
        if (text.isEmpty()) {
            mNames.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (String item : itemsCopy) {
                if (item.toLowerCase().contains(text)) {
                    mNames.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.colorView)
        ImageView tileImageView;
        @BindView(R.id.name)
        TextView nameTextView;

        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
