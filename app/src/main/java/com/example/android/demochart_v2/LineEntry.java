package com.example.android.demochart_v2;

import android.graphics.drawable.Drawable;
import android.os.Parcel;

import com.github.mikephil.charting.data.Entry;

/**
 * Created by Android on 3/13/2018.
 */

public class LineEntry extends Entry{
    public LineEntry() {
    }

    public LineEntry(float x, float y) {
        super(x, y);
    }

    public LineEntry(float x, float y, Object data) {
        super(x, y, data);
    }

    public LineEntry(float x, float y, Drawable icon) {
        super(x, y, icon);
    }

    public LineEntry(float x, float y, Drawable icon, Object data) {
        super(x, y, icon, data);
    }

    public LineEntry(Parcel in) {
        super(in);
    }
}
