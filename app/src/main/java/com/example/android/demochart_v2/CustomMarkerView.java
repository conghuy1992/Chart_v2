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
    private LinearLayout layoutOne, layoutTwo;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutOne = (LinearLayout) findViewById(R.id.layoutOne);
        layoutTwo = (LinearLayout) findViewById(R.id.layoutTwo);
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

        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.GONE);
        space = 10f;

        float top = e.coordinatesY - distance;
        float bottom = e.coordinatesY + distance;
        if (e instanceof BarEntry || e instanceof LineEntry) {

        } else {
            //  Entry -> use for scatter chart
            top = e.coordinatesY - e.heightImage;
            bottom = e.coordinatesY + e.heightImage;
            space = e.heightImage;
        }

        /**
         * if barchart -> top alway < 0
         * */

        Log.d(TAG, "centerY: " + e.coordinatesY);
        Log.d(TAG, "top:" + top);
        Log.d(TAG, "bottom:" + bottom);
        if (top > 0 && coordinatesY >= top && coordinatesY <= bottom) {
            if (e instanceof LineEntry) {
                tvTitle.setText("LineEntry");
                layoutOne.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setText("ICON");
                layoutTwo.setVisibility(View.VISIBLE);
            }
        } else {
//            layoutOne.setVisibility(View.GONE);
        }
        super.refreshContent(e, highlight);
    }

    float space;

    @Override
    public MPPointF getOffset() {
        MPPointF offset = new MPPointF(-(getWidth() / 2), -getHeight() - 10 - space / 1.5f);
        return offset;
    }
}
