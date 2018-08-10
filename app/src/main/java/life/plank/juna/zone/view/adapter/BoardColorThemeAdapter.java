package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.BaseRecyclerView;

public class BoardColorThemeAdapter extends BaseRecyclerView.Adapter<BoardColorThemeAdapter.BoardColorThemeViewHolder> {

    private static int selectedIndex;
    private List<String> boardColorList;
    private Picasso picasso;

    public BoardColorThemeAdapter(Picasso picasso) {
        this.boardColorList = new ArrayList<>();
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

    public void update(List<String> boardColorList) {
        this.boardColorList.addAll(boardColorList);
        notifyDataSetChanged();
    }

    static class BoardColorThemeViewHolder extends BaseRecyclerView.ViewHolder {

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