package life.plank.juna.zone.view.adapter.board.creation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.BoardColorFactory;

public class BoardColorThemeAdapter extends RecyclerView.Adapter<BoardColorThemeAdapter.BoardColorThemeViewHolder> {

    private static int selectedIndex = -1;
    private List<String> boardColorList;

    public BoardColorThemeAdapter() {
        this.boardColorList = BoardColorFactory.getAllColors();
    }

    @Override
    public BoardColorThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardColorThemeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_color_theme, parent, false),
                this
        );
    }

    @Override
    public void onBindViewHolder(BoardColorThemeViewHolder holder, int position) {
        holder.colorThemeImageView.setImageDrawable(new ColorDrawable(Color.parseColor(boardColorList.get(position))));
        holder.imageSelectionMarker.setVisibility(selectedIndex == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return boardColorList.size();
    }

    public String getSelectedColor() {
        if (selectedIndex >= 0)
            return boardColorList.get(selectedIndex);
        return null;
    }

    static class BoardColorThemeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.color_theme_image_view)
        CircleImageView colorThemeImageView;
        @BindView(R.id.image_selection_marker)
        ImageView imageSelectionMarker;
        private WeakReference<BoardColorThemeAdapter> ref;

        BoardColorThemeViewHolder(View itemView, BoardColorThemeAdapter adapter) {
            super(itemView);
            ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        public void onColorThemeSelected() {
            int previousSelection = selectedIndex;
            imageSelectionMarker.setVisibility(selectedIndex == getAdapterPosition() ? View.INVISIBLE : View.VISIBLE);
            selectedIndex = getAdapterPosition();
            ref.get().notifyItemChanged(previousSelection);
            ref.get().notifyItemChanged(selectedIndex);
        }
    }
}