package life.plank.juna.zone.viewmodel;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/6/2018.
 */

public class LiveZoneGridModel {
    private String data;
    private int image;


    public LiveZoneGridModel(String data, int image) {
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

    public static List<LiveZoneGridModel> getLiveZoneData() {
        ArrayList<LiveZoneGridModel> liveZoneGridModels = new ArrayList<>();
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_one));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_two));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_one));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_two));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_five));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_three));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_one));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_two));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_four));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_four));
        liveZoneGridModels.add(new LiveZoneGridModel("text", R.drawable.ic_grid_five));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_six));
        return liveZoneGridModels;
    }
}
