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
import life.plank.juna.zone.data.network.model.User;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    ArrayList<User> usernameListCopy = new ArrayList<>();
    private List<Integer> profilePictureList;
    private List<User> userList;
    private Context context;

    public SearchViewAdapter(List<User> userList, Context context) {
        this.context = context;
        this.userList = userList;
        usernameListCopy.addAll(this.userList);
    }

    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.SearchViewHolder holder, int position) {
        holder.usernameTextView.setText(userList.get(position).getDisplayName());

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
        return userList.size();
    }

    public void update(List<User> users) {
        this.userList.addAll(users);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        userList.clear();
        if (text.isEmpty()) {
            userList.addAll(usernameListCopy);
        } else {
            text = text.toLowerCase();
            for (User user : usernameListCopy) {
                if (user.getDisplayName().toLowerCase().contains(text)) {
                    userList.add(user);
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
