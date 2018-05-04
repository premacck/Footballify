package life.plank.juna.zone.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.ScrubberConstants;

public class GraphFragment extends Fragment {
    RelativeLayout linearLayout;
    LineChart lineChart;
    ArrayList<Entry> values = new ArrayList<>();
    long now;
    private HashMap<Integer, ScrubberViewData> graphData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        linearLayout = view.findViewById(R.id.root);
        lineChart = view.findViewById(R.id.line_chart);
        setUpGraph();
        return view;
    }

    private void setUpGraph() {
        setUpChart();
    }

    private void setUpChart() {
        graphData = new HashMap<>();
        ScrubberConstants.getHighLightsMatchOne(graphData);
        // no description text
        lineChart.getDescription().setEnabled(false);
        // enable touch gestures
        //  lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationEnabled(false);
        //lineChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        // set an alternative background color
        //lineChart.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient));
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);
        setData(100, 100);
        lineChart.animateXY(100000, 0);
        // dont forget to refresh the drawing
        //mChart.invalidate();
        // get the legend (only possible after setting data)
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        //xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f); // one hour

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 255, 255));
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        lineChart.invalidate();
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    private void setData(int count, float range) {
        // now in hours
        now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        float from = now;
        // count = hours
        float to = now + count;
        // increment by 1 hour
        for (float chartX = from; chartX < to; chartX++) {
            float chartY = getRandom(range, 0);
            values.add(new Entry(chartX, chartY)); // add one entry per hour
            LineDataSet set1 = new LineDataSet(values, "DataSet 1");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setValueTextColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(1.5f);
            set1.setDrawCircles(false);
            set1.setDrawFilled(true);
            //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);
            //set1.setFillDrawable(drawable);
            set1.setDrawValues(false);
            set1.setFillAlpha(65);
            //set1.setFillColor(R.drawable.gradient);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            //create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.YELLOW);
            data.setValueTextSize(9f);
            lineChart.setData(data);
            lineChart.invalidate();
        }
    }
}
