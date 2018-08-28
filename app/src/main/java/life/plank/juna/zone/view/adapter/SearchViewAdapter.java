package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    private Context context;

    public SearchViewAdapter(List<Integer> profilePictureList, List<String> usernameList, Context context) {
        this.context = context;
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

        holder.profileImageView.setOnClickListener(view -> {
            if (holder.followTick.getVisibility() == View.VISIBLE) {
                holder.followTick.setVisibility(View.INVISIBLE);
                holder.profileImageView.setAlpha(255);
                holder.usernameTextView.setVisibility(View.VISIBLE);
                holder.usernameTextView.setTextColor(context.getResources().getColor(R.color.grey));
                holder.profileImageView.clearColorFilter();

            } else {
                holder.followTick.setVisibility(View.VISIBLE);
                holder.profileImageView.setAlpha(160);
                holder.usernameTextView.setTextColor(Color.BLACK);
                holder.profileImageView.setColorFilter(context.getResources().getColor(R.color.red_pink), PorterDuff.Mode.LIGHTEN);
            }
        });
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
        @BindView(R.id.follow_image_view)
        ImageView followTick;

        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
