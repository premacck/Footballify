package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Board;

public class MyBoardsAdapter extends RecyclerView.Adapter<MyBoardsAdapter.MyBoardsViewHolder> {

    private List<Board> boardList;
    private Picasso picasso;

    public MyBoardsAdapter(Picasso picasso) {
        this.boardList = new ArrayList<>();
        this.picasso = picasso;
    }

    @Override public MyBoardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyBoardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_boards, parent, false));
    }

    @Override public void onBindViewHolder(MyBoardsViewHolder holder, int position) {
        picasso.load(boardList.get(position).getColor()).into(holder.boardIcon);
    }

    @Override public int getItemCount() {
        return 0;
    }

    /**
     * Use this method to update the recyclerView's contents instead of using and managing a list in the Activity (where it doesn't really belong).
     * @param boardList the list to update (usually fetched from the server).
     */
    public void update(List<Board> boardList) {
        this.boardList = boardList;
        notifyDataSetChanged();
    }

    static class MyBoardsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.board_icon) CircleImageView boardIcon;

        MyBoardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
