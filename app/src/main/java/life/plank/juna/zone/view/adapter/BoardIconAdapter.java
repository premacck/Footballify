package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;

import static android.view.LayoutInflater.from;

public class BoardIconAdapter extends RecyclerView.Adapter<BoardIconAdapter.BoardIconViewHolder> {

    private static int selectedIndex;
    private List<String> boardIconList;
    private Picasso picasso;

    public BoardIconAdapter(List<String> boardIconList, Picasso picasso) {
        this.boardIconList = boardIconList;
        this.picasso = picasso;
    }

    @Override public BoardIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardIconViewHolder(from(parent.getContext()).inflate(R.layout.item_board_icon, parent, false));
    }

    @Override public void onBindViewHolder(BoardIconViewHolder holder, int position) {
        picasso.load(boardIconList.get(position)).into(holder.colorThemeImageView);
        holder.imageSelectionMarker.setVisibility(selectedIndex == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override public int getItemCount() {
        return boardIconList.size();
    }

    static class BoardIconViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_image_view) CircleImageView colorThemeImageView;
        @BindView(R.id.image_selection_marker) ImageView imageSelectionMarker;

        BoardIconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.color_theme_image_view) public void onColorThemeSelected() {
            imageSelectionMarker.setVisibility(selectedIndex == getAdapterPosition() ? View.INVISIBLE : View.VISIBLE);
            selectedIndex = getAdapterPosition();
        }
    }

    public String getSelectedColor() {
        return boardIconList.get(selectedIndex);
    }
}
