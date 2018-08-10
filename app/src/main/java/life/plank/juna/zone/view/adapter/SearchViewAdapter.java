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

    ArrayList<String> usernameListCopy = new ArrayList<>();
    private List<Integer> profilePictureList;
    private List<String> usernameList;

    public SearchViewAdapter(List<Integer> profilePictureList, List<String> usernameList) {
        this.profilePictureList = profilePictureList;
        this.usernameList = usernameList;
        usernameListCopy.addAll(this.usernameList);
    }

    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.SearchViewHolder holder, int position) {
        holder.profileImageView.setImageResource(profilePictureList.get(position));
        holder.usernameTextView.setText(usernameList.get(position));
    }

    @Override
    public int getItemCount() {
        return usernameList.size();
    }

    public void filter(String text) {
        usernameList.clear();
        if (text.isEmpty()) {
            usernameList.addAll(usernameListCopy);
        } else {
            text = text.toLowerCase();
            for (String item : usernameListCopy) {
                if (item.toLowerCase().contains(text)) {
                    usernameList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        ImageView profileImageView;
        @BindView(R.id.username)
        TextView usernameTextView;

        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
