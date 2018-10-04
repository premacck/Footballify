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

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.interfaces.OnItemClickListener;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    private List<User> userList;
    private Context context;
    private Picasso picasso;
    private OnItemClickListener onItemClickListener;

    public SearchViewAdapter(List<User> userList, Context context, OnItemClickListener onItemClickListener, Picasso picasso) {
        this.context = context;
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
        this.picasso = picasso;
    }

    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_and_title, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.SearchViewHolder holder, int position) {
        holder.usernameTextView.setText(userList.get(position).getDisplayName());
        picasso.load(userList.get(position).getProfilePictureUrl())
                .placeholder(R.drawable.ic_default_profile)
                .error(R.drawable.ic_default_profile)
                .into(holder.profileImageView);
        holder.profileImageView.setOnClickListener(view -> {
            if (holder.followTick.getVisibility() == View.VISIBLE) {
                holder.followTick.setVisibility(View.INVISIBLE);
                holder.profileImageView.setAlpha(255);
                holder.usernameTextView.setVisibility(View.VISIBLE);
                holder.usernameTextView.setTextColor(context.getResources().getColor(R.color.grey));
                holder.profileImageView.clearColorFilter();
                onItemClickListener.onItemClicked(userList.get(position).getObjectId(), false);
            } else {
                holder.followTick.setVisibility(View.VISIBLE);
                holder.profileImageView.setAlpha(160);
                holder.usernameTextView.setTextColor(Color.BLACK);
                holder.profileImageView.setColorFilter(context.getResources().getColor(R.color.red_pink), PorterDuff.Mode.LIGHTEN);
                onItemClickListener.onItemClicked(userList.get(position).getObjectId(), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void update(List<User> users) {
        userList.clear();
        this.userList.addAll(users);
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView profileImageView;
        @BindView(R.id.title)
        TextView usernameTextView;
        @BindView(R.id.follow_image_view)
        ImageView followTick;

        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
