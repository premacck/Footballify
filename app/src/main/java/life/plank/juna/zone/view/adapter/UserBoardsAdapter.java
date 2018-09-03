package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.graphics.Color;
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
import life.plank.juna.zone.data.network.model.Board;

public class UserBoardsAdapter extends RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder> {

    private List<Board> boardList;
    private Context context;

    public UserBoardsAdapter(List<Board> boardList, Context context) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public UserBoardsAdapter.UserBoardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserBoardsAdapter.UserBoardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(UserBoardsAdapter.UserBoardsViewHolder holder, int position) {
        holder.usernameTextView.setText(boardList.get(position).getDisplayname());
        holder.profileImageView.setBackgroundColor(Color.parseColor(boardList.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void update(List<Board> users) {
        boardList.clear();
        this.boardList.addAll(users);
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

