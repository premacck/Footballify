package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 1/31/2018.
 */

public class LiveZoneSliderView extends BaseSliderView {
    private String liveZoneSliderData;
    private Context context;

    public LiveZoneSliderView(Context context, String data) {
        super(context);
        this.liveZoneSliderData = data;
        this.context = context;
    }

    @Override
    public View getView() {
        View v;
        if (liveZoneSliderData.contentEquals("text")) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row, null);

        } else {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row1, null);
        }
        bindEventAndShow(v, null);
        return v;
    }

}
