package com.example.android.demochart_v2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.data.ScatterDto;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private CombinedChart mChart;

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
//            "13","14","15","16","17","18","19","20"
    };
    private List<DemoDto> list;

    private void initListSample() {
        list = new ArrayList<>();

        for (String s : mMonths) {
            DemoDto obj = new DemoDto();
            obj.month = s;
            list.add(obj);
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.content_main);

        initListSample();

        mChart = (CombinedChart) findViewById(R.id.chart1);
        final CustomMarkerView mv = new CustomMarkerView(this, R.layout.marker_custom);
        mChart.setMarker(mv);

        mChart.setScaleEnabled(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.zoomDefault();

        mChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
//                        Log.d(TAG, "ACTION_DOWN");
                        isMove = false;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
//                        Log.d(TAG, "ACTION_MOVE");
                        isMove = true;
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (!isMove) {
                            mv.updateView(y);
                            Log.d(TAG, "Y:" + y);
                            Log.d(TAG, "ACTION_UP");
                        }
                        break;
                    }
                }
                return false;
            }
        });

//        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                if (e instanceof ScatterDto) Log.d(TAG,"Scatter");
//                else if (e instanceof LineEntry) Log.d(TAG,"LineEntry");
//                else if (e instanceof BarEntry) Log.d(TAG,"BarEntry");
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });

        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
//        mChart.setDrawValueAboveBar(false);
        mChart.getXAxis().setDrawGridLines(false);

//        mChart.getXAxis().setEnabled(false);

        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawLabels(false);

        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisLeft().setDrawLabels(false);

        mChart.getXAxis().setDrawAxisLine(false);


        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.LINE, DrawOrder.SCATTER
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);

        xAxis.setLabelCount(list.size());

        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return list.get((int) value % list.size()).month;

            }
        });

        CombinedData data = new CombinedData();
        data.setData(generateBarData());
        data.setData(generateLineData());
//        data.setData(generateBarCustomData());
        data.setData(generateScatterData());
//        data.setData(generateCandleData());

        float xAxisPadding = 0.45f;
        xAxis.setAxisMinimum(-xAxisPadding);
        xAxis.setAxisMaximum(data.getXMax() + xAxisPadding);


//        mChart.xAxis.axisMinimum = -xAxisPadding
//        mChart.xAxis.axisMaximum = combinedChartData.xMax + xAxisPadding

        mChart.setData(data);
        mChart.invalidate();
//        Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        mChart.setRenderer(new CombinedChartRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler(), starBitmap));

    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < list.size(); index++) {
            float y = list.get(index).barY - 2f;
            if (y < 0) y = 0;
            LineEntry entry = new LineEntry(index, y);
            entries.add(entry);
        }

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawVerticalHighlightIndicator(false);

        int c = Color.parseColor("#e06e5c");
        set.setColor(c);
        set.setCircleColor(c);
        set.setCircleRadius(5f);
        set.setFillColor(c);
        set.setLineWidth(2f);
        set.setCircleColorHole(c);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(1f);
//        set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        for (int index = 0; index < list.size(); index++) {
            float y = getRandom(15, 5);
            list.get(index).barY = y;
            BarEntry obj = new BarEntry(index, y);
            entries1.add(obj);
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
//        set1.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE,
//                Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE,Color.RED, Color.GREEN});


        int color[] = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String s = "#8bb39c";
            if (i % 2 == 0) s = "#908e94";
            color[i] = Color.parseColor(s);
        }
        set1.setColors(color);
        set1.setValueTextSize(1f);
        set1.setDrawValues(false);
//        set1.setValueTextColor(Color.rgb(60, 220, 78));

//        set1.setValueTextSize(10f);
//        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        set1.setHighLightAlpha(0);

        BarData d = new BarData(set1);

//        BarData d = new BarData(set1);
//        d.setBarWidth(barWidth);


        return d;
    }

    protected ScatterData generateScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < list.size(); index++) {
            float y = list.get(index).barY + 0.7f;
            Entry entry = new Entry(index, y);
            entry.visible = index % 2 == 0 ? true : false;
            Drawable myIcon = getResources().getDrawable(R.drawable.ic_location);
            entry.setIcon(myIcon);
            entries.add(entry);
        }

        ScatterDataSet set = new ScatterDataSet(entries, "Scatter DataSet");
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawVerticalHighlightIndicator(false);
        set.setColors(Color.RED);
        set.setScatterShapeSize(0f);
        set.setDrawValues(false);
        set.setDrawIcons(true);
        set.setValueTextSize(10f);
        d.addDataSet(set);

        return d;
    }

    protected CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();

        for (int index = 0; index < list.size(); index += 2)
            entries.add(new CandleEntry(index + 1f, 90, 70, 85, 75f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setDecreasingColor(Color.rgb(142, 150, 175));
        set.setShadowColor(Color.DKGRAY);
        set.setBarSpace(0.3f);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }

    protected BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();

        ArrayList<BubbleEntry> entries = new ArrayList<BubbleEntry>();

        for (int index = 0; index < list.size(); index++) {
            float y = getRandom(10, 40);
            float size = getRandom(50, 50);
            Log.d(TAG, "size:" + size);
            entries.add(new BubbleEntry(index, y, 5));
        }

        BubbleDataSet set = new BubbleDataSet(entries, "Bubble DataSet");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        set.setValueTextSize(10f);
//        set.setValueTextColor(Color.WHITE);
//        set.setHighlightCircleWidth(1.5f);
//        set.setDrawValues(true);
        bd.addDataSet(set);

        return bd;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                Log.d(TAG,"ACTION_DOWN");
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                Log.d(TAG,"ACTION_UP");
//            }
//        }
//        return super.onTouchEvent(event);
//    }

}
