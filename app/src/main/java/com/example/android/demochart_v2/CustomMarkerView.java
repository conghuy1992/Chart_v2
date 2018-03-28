package com.example.android.demochart_v2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Android on 3/13/2018.
 */

public class CustomMarkerView extends MarkerView {
    private String TAG = "CustomMarkerView";
    private TextView tvTitle;
    private LinearLayout root;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        root = (LinearLayout) findViewById(R.id.root);
        Log.d(TAG, "CustomMarkerView");
    }

    private float coordinatesY;

    public void updateView(float coordinatesY) {
        this.coordinatesY = coordinatesY;
        Log.d(TAG, "updateView:" + coordinatesY);
    }

    private float distance = 50; // px

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        float top = e.coordinatesY - distance;
        float bottom = e.coordinatesY + distance;
        if (e instanceof BarEntry || e instanceof LineEntry) {

        } else {
            //  Entry -> use for scatter chart
            top = e.coordinatesY - e.heightImage;
            bottom = e.coordinatesY + e.heightImage;
        }

        /**
         * if barchart top < 0
         * */

        Log.d(TAG,"centerY: "+e.coordinatesY);
        Log.d(TAG, "top:" + top);
        Log.d(TAG, "bottom:" + bottom);
        if (top > 0 && coordinatesY >= top && coordinatesY <= bottom) {
            root.setVisibility(View.VISIBLE);

            if (e instanceof LineEntry) {
                tvTitle.setText("LineEntry");
            } else {
                tvTitle.setText("ICON");
                Log.d(TAG, "ICON");
            }
        } else {
            root.setVisibility(View.GONE);
        }
        super.refreshContent(e, highlight);
    }
}
