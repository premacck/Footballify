package life.plank.juna.zone.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;

import static android.view.LayoutInflater.from;

public class BoardIconAdapter extends RecyclerView.Adapter<BoardIconAdapter.BoardIconViewHolder> {

    private static int selectedIndex;
    private List<Drawable> boardIconList;

    public BoardIconAdapter() {
        ArrayList<Drawable> boardIconList = new ArrayList<>();
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_beer));
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_football));
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_tshirt));
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_cup));
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_music));
        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_guitar));
        this.boardIconList = boardIconList;
    }

    @Override
    public BoardIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardIconViewHolder(from(parent.getContext()).inflate(R.layout.item_board_icon, parent, false), this);
    }

    @Override
    public void onBindViewHolder(BoardIconViewHolder holder, int position) {
        holder.colorThemeImageView.setImageDrawable(boardIconList.get(position));
        holder.imageSelectionMarker.setVisibility(selectedIndex == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return boardIconList.size();
    }

    static class BoardIconViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_image_view)
        CircleImageView colorThemeImageView;
        @BindView(R.id.image_selection_marker)
        ImageView imageSelectionMarker;
        private WeakReference<BoardIconAdapter> ref;

        BoardIconViewHolder(View itemView, BoardIconAdapter adapter) {
            super(itemView);
            ref = new WeakReference<>(adapter);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.icon_image_view})
        public void onColorThemeSelected() {
            int previousSelection = selectedIndex;
            imageSelectionMarker.setVisibility(selectedIndex == getAdapterPosition() ? View.INVISIBLE : View.VISIBLE);
            selectedIndex = getAdapterPosition();
            ref.get().notifyItemChanged(previousSelection);
            ref.get().notifyItemChanged(selectedIndex);
        }
    }
}