package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import life.plank.juna.zone.view.activity.InviteToBoardActivity;
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment;

import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class BoardMembersViewAdapter extends RecyclerView.Adapter<BoardMembersViewAdapter.BoardMembersViewHolder> {

    public static Bitmap parentViewBitmap = null;
    private Picasso picasso;
    private List<User> userList;
    private Context context;
    private String boardId;
    private String displayName;
    private String boardName;
    private PrivateBoardInfoFragment fragment;

    public BoardMembersViewAdapter(List<User> userList, Context context, String boardId, PrivateBoardInfoFragment fragment, String displayName,
                                   Picasso picasso, String boardName) {
        this.context = context;
        this.userList = userList;
        this.boardId = boardId;
        this.fragment = fragment;
        this.displayName = displayName;
        this.picasso = picasso;
        this.boardName = boardName;
    }

    @Override
    public BoardMembersViewAdapter.BoardMembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardMembersViewAdapter.BoardMembersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(BoardMembersViewAdapter.BoardMembersViewHolder holder, int position) {
        holder.usernameTextView.setText(userList.get(position).getDisplayName());

        picasso.load(userList.get(position).getProfilePictureUrl())
                .into(holder.profileImageView);

        if (userList.get(position).getDisplayName().equals(context.getString(R.string.invite_string))) {
            holder.profileImageView.setBackground(context.getDrawable(R.drawable.new_board_circle));
        }

        holder.profileImageView.setOnClickListener(view -> {
            if (userList.get(position).getDisplayName().equals(context.getString(R.string.invite_string))) {
                parentViewBitmap = loadBitmap(fragment.getActivity().getWindow().getDecorView(), fragment.getActivity().getWindow().getDecorView(), context);
                Intent inviteToBoard = new Intent(context, InviteToBoardActivity.class);
                inviteToBoard.putExtra(context.getString(R.string.intent_board_id), boardId);
                inviteToBoard.putExtra(context.getString(R.string.board_title), context.getString(R.string.invite_people_to) + " " + boardName + " " +
                        context.getString(R.string.board));
                inviteToBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(inviteToBoard);
            } else if (!userList.get(position).getDisplayName().equals(displayName)) {
                PrivateBoardInfoFragment.onClickProfileImage(view, userList.get(position).getObjectId(), position);
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

    class BoardMembersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView profileImageView;
        @BindView(R.id.title)
        TextView usernameTextView;

        BoardMembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
