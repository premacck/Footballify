package life.plank.juna.zone.view.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.Image;

import static android.view.LayoutInflater.from;

public class BoardIconAdapter extends RecyclerView.Adapter<BoardIconAdapter.BoardIconViewHolder> {

    private static int selectedIndex = -1;
    public List<String> boardIconList = new ArrayList<>();;

    public BoardIconAdapter() {
        //TODO: Will be uncommented after changes are made on the backend
//        ArrayList<Drawable> boardIconList = new ArrayList<>();
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_beer));
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_football));
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_tshirt));
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_cup));
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_music));
//        boardIconList.add(ZoneApplication.getContext().getDrawable(R.drawable.ic_board_guitar));
        this.boardIconList = boardIconList;
    }

    @Override
    public BoardIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardIconViewHolder(from(parent.getContext()).inflate(R.layout.item_board_icon, parent, false), this);
    }

    @Override
    public void onBindViewHolder(BoardIconViewHolder holder, int position) {
        try {
            File imgFile = new File(boardIconList.get(position));
            if (imgFile.exists()) {
                Bitmap bitmap = new Image().compress(imgFile, boardIconList.get(position));
                Drawable imageDrawable = new BitmapDrawable(ZoneApplication.getContext().getResources(), bitmap);
                holder.colorThemeImageView.setImageDrawable(imageDrawable);
            }
        } catch (Exception e) {
            Log.e("TAG", "CAMERA_IMAGE_RESULT : " + e);
            Toast.makeText(ZoneApplication.getContext(), R.string.could_not_process_image, Toast.LENGTH_LONG).show();
        }
        holder.imageSelectionMarker.setVisibility(selectedIndex == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return boardIconList.size();
    }

    public String getSelectedPath() {
        if (selectedIndex >= 0)
            return boardIconList.get(selectedIndex);
        return null;
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

        @OnClick({R.id.root_layout})
        public void onColorThemeSelected() {
            int previousSelection = selectedIndex;
            imageSelectionMarker.setVisibility(selectedIndex == getAdapterPosition() ? View.INVISIBLE : View.VISIBLE);
            selectedIndex = getAdapterPosition();
            ref.get().notifyItemChanged(previousSelection);
            ref.get().notifyItemChanged(selectedIndex);
        }
    }
}