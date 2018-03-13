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
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        Log.d(TAG, "refreshContent");
        if (e instanceof BarEntry) {
            tvTitle.setText("BarEntry");
        } else if (e instanceof LineEntry) {
            tvTitle.setText("LineEntry");
        } else {
            tvTitle.setText("ICON");
        }

//        tvTitle.setText("" + e.getX());
    }

}
