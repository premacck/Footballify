package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.User;

public class UserBoardsAdapter extends RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder> {

    private List<User> userList;
    private Context context;

    public UserBoardsAdapter(List<User> userList, Context context) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public UserBoardsAdapter.UserBoardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserBoardsAdapter.UserBoardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(UserBoardsAdapter.UserBoardsViewHolder holder, int position) {
        //TODO: display actual user details
        holder.usernameTextView.setText("UserName");
        holder.profileImageView.setBackground(context.getDrawable(R.drawable.ic_profile_dummy));

    }

    @Override
    public int getItemCount() {
        //TODO: Replace with userList.size();
        return 20;
    }

    public void update(List<User> users) {
        userList.clear();
        this.userList.addAll(users);
        notifyDataSetChanged();
    }

    public class UserBoardsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        ImageView profileImageView;
        @BindView(R.id.username)
        TextView usernameTextView;

        UserBoardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

