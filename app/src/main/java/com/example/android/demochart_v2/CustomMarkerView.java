package com.example.android.demochart_v2;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDto;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by Android on 3/13/2018.
 */

public class CustomMarkerView extends MarkerView {
    private String TAG = "CustomMarkerView";
    private TextView tvTitle;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        Log.d(TAG,"CustomMarkerView");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {


        if (e instanceof BarEntry) {
            tvTitle.setText("BarEntry");
        } else if (e instanceof LineEntry) {
            tvTitle.setText("LineEntry");
        } else {
            tvTitle.setText("ICON");
        }
        Log.d(TAG, "refreshContent");
//        tvTitle.setText("" + e.getX());

        super.refreshContent(e, highlight);
    }

}
