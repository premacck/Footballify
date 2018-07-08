package life.plank.juna.zone.viewmodel;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/6/2018.
 */

public class BoardGridModel {
    private String data;
    private int image;


    public BoardGridModel(String data, int image) {
        this.data = data;
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static List<BoardGridModel> getLiveZoneData() {
        ArrayList<BoardGridModel> boardGridModels = new ArrayList<>();
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_one));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_two));
        boardGridModels.add(new BoardGridModel("image", R.drawable.ic_grid_one));
        boardGridModels.add(new BoardGridModel("image", R.drawable.ic_grid_two));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_five));
        boardGridModels.add(new BoardGridModel("image", R.drawable.ic_grid_three));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_one));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_two));
        boardGridModels.add(new BoardGridModel("image", R.drawable.ic_grid_four));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_four));
        boardGridModels.add(new BoardGridModel("text", R.drawable.ic_grid_five));
        boardGridModels.add(new BoardGridModel("image", R.drawable.ic_grid_six));
        return boardGridModels;
    }
}
