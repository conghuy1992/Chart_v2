package com.github.mikephil.charting.data;

import com.github.mikephil.charting.data.Entry;

/**
 * Created by Android on 3/12/2018.
 */

public class ScatterDto extends Entry {
    public boolean visible;

    public ScatterDto(float x, float y, boolean visible) {
        super(x, y);
        this.visible = visible;
    }
}
