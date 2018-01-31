package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

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
        this.liveZoneSliderData = liveZoneSliderData;
        this.context = context;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row, null);
        ImageView target = (ImageView) v.findViewById(R.id.liveZoneSliderImage);

        bindEventAndShow(v, target);
        return v;
    }

}
