package life.plank.juna.zone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class BoardColorThemeAdapter extends RecyclerView.Adapter<BoardColorThemeAdapter.BoardColorThemeViewHolder> {

    private static int selectedIndex;
    private List<String> boardColorList;
    private Picasso picasso;

    public BoardColorThemeAdapter(List<String> boardColorList, Picasso picasso) {
        this.boardColorList = boardColorList;
        this.picasso = picasso;
    }

    @Override public BoardColorThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardColorThemeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_color_theme, parent, false)
        );
    }

    @Override public void onBindViewHolder(BoardColorThemeViewHolder holder, int position) {
        picasso.load(boardColorList.get(position)).into(holder.colorThemeImageView);
        holder.imageSelectionMarker.setVisibility(selectedIndex == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override public int getItemCount() {
        return boardColorList.size();
    }

    static class BoardColorThemeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.color_theme_image_view) CircleImageView colorThemeImageView;
        @BindView(R.id.image_selection_marker) ImageView imageSelectionMarker;

        BoardColorThemeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.color_theme_image_view) public void onColorThemeSelected() {
            imageSelectionMarker.setVisibility(selectedIndex == getAdapterPosition() ? View.INVISIBLE : View.VISIBLE);
            selectedIndex = getAdapterPosition();
        }
    }

    public String getSelectedColor() {
        return boardColorList.get(selectedIndex);
    }
}
