package life.plank.juna.zone.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.ScrubberConstants;

public class GraphActivity extends AppCompatActivity {

    LineChart lineChart;
    long now;
    ArrayList<Entry> values = new ArrayList<Entry>();
    int data = 0;
    RelativeLayout linearLayout;
    private HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        //linearLayout = findViewById(R.id.root);
        //setUpChart();
        //setUpThread();
    }
/*
    private void setUpThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        now = now + 1;
                        float y = getRandom(100, 0);
                        data = data + 1;
                        values.add(data, new Entry(now, y));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data % 20 == 0) {
                                    ImageView imageView = new ImageView(GraphActivity.this);
                                    imageView.setImageResource(R.drawable.ic_aston_villa_logo);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                                    layoutParams.setMargins(data, 0, 0, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    linearLayout.addView(imageView);
                                }
                                lineChart.invalidate();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setUpChart() {
        scrubberViewDataHolder = new HashMap<>();
        ScrubberConstants.getHighLightsMatchOne(scrubberViewDataHolder);
        lineChart = (LineChart) findViewById(R.id.line_chart);
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
    }*/
}
