package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDto;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class ScatterChartRendererCustom extends LineScatterCandleRadarRenderer {
    protected String TAG = "ScatterChartRendererCustom";
    protected ScatterDataProvider mChart;
    protected Bitmap bitmap;

    public ScatterChartRendererCustom(ScatterDataProvider chart, ChartAnimator animator,
                                      ViewPortHandler viewPortHandler, Bitmap bitmap) {
        super(animator, viewPortHandler);
        mChart = chart;
        this.bitmap = bitmap;
    }

    @Override
    public void initBuffers() {
    }

    @Override
    public void drawData(Canvas c) {

        ScatterData scatterData = mChart.getScatterData();

        for (IScatterDataSet set : scatterData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    float[] mPixelBuffer = new float[2];
    float maxWidth = 0;

    protected void drawDataSet(Canvas c, IScatterDataSet dataSet) {

        ViewPortHandler viewPortHandler = mViewPortHandler;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        IShapeRenderer renderer = dataSet.getShapeRenderer();
        if (renderer == null) {
            Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
            return;
        }

        int max = (int) (Math.min(
                Math.ceil((float) dataSet.getEntryCount() * mAnimator.getPhaseX()),
                (float) dataSet.getEntryCount()));

        if (max > 1) {
            Entry e = dataSet.getEntryForIndex(0);
            mPixelBuffer[0] = e.getX();
            trans.pointValuesToPixel(mPixelBuffer);
            float x1 = mPixelBuffer[0];

            e = dataSet.getEntryForIndex(1);
            mPixelBuffer[0] = e.getX();
            trans.pointValuesToPixel(mPixelBuffer);
            float x2 = mPixelBuffer[0];

            Log.d(TAG, "x1:" + x1);
            Log.d(TAG, "x2:" + x2);
            maxWidth = x2 - x1;

        }
//
        Log.d(TAG, "maxWidth:" + maxWidth);

        for (int i = 0; i < max; i++) {

            Entry e = dataSet.getEntryForIndex(i);

            mPixelBuffer[0] = e.getX();
            mPixelBuffer[1] = e.getY() * phaseY;

            trans.pointValuesToPixel(mPixelBuffer);

            if (!viewPortHandler.isInBoundsRight(mPixelBuffer[0]))
                break;

            if (!viewPortHandler.isInBoundsLeft(mPixelBuffer[0])
                    || !viewPortHandler.isInBoundsY(mPixelBuffer[1]))
                continue;

            mRenderPaint.setColor(dataSet.getColor(i / 2));

            boolean visible = false;
            if (e instanceof ScatterDto) {
                visible = ((ScatterDto) e).visible;
            }
            if (visible)
//                renderer.renderShape(
//                        c, dataSet, mViewPortHandler,
//                        mPixelBuffer[0], mPixelBuffer[1],
//                        mRenderPaint);
//
//            mRenderPaint.setStyle(Paint.Style.FILL);

//            c.drawCircle(mPixelBuffer[0], mPixelBuffer[1] ,80, mRenderPaint);

                drawImage(c, bitmap, mPixelBuffer[0], mPixelBuffer[1]);

//            Log.d(TAG, "i:" + i);

//            Log.d(TAG, "x:" + mPixelBuffer[0]);
//            Log.d(TAG, "y:" + mPixelBuffer[1]);
        }
    }

    private Bitmap scaleBarImage(Bitmap bitmap, int width) {
//        BarBuffer buffer = mBarBuffers[0];
//        float firstLeft = buffer.buffer[0];
//        float firstRight = buffer.buffer[2];
//        int firstWidth = (int) Math.ceil(firstRight - firstLeft);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Log.d(TAG, "w1:" + w);
        Log.d(TAG, "h1:" + h);

//        1000 500
//        500  250

        if (w > width) {
            float ratio = (float) width / w;
            w = width;
            h = (int) (h * ratio);
            Log.d(TAG, "ratio:" + ratio);
            Log.d(TAG, "w2:" + w);
            Log.d(TAG, "h2:" + h);
        }

        Log.d(TAG, "w3:" + w);
        Log.d(TAG, "h3:" + h);

        return Bitmap.createScaledBitmap(bitmap, w, h, false);


    }

    protected void drawImage(Canvas c, Bitmap image, float x, float y) {
        if (image != null) {
            if (maxWidth != 0) image = scaleBarImage(image, (int) maxWidth);

            float w = image.getWidth();
            x = x - w / 2f;

            c.drawBitmap(image, x, y, null);
        } else {
        }
    }

    @Override
    public void drawValues(Canvas c) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<IScatterDataSet> dataSets = mChart.getScatterData().getDataSets();

            for (int i = 0; i < mChart.getScatterData().getDataSetCount(); i++) {

                IScatterDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                mXBounds.set(mChart, dataSet);

                float[] positions = mChart.getTransformer(dataSet.getAxisDependency())
                        .generateTransformedValuesScatter(dataSet,
                                mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

                float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {

                    if (!mViewPortHandler.isInBoundsRight(positions[j]))
                        break;

                    // make sure the lines don't do shitty things outside bounds
                    if ((!mViewPortHandler.isInBoundsLeft(positions[j])
                            || !mViewPortHandler.isInBoundsY(positions[j + 1])))
                        continue;

                    Entry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(c,
                                dataSet.getValueFormatter(),
                                entry.getY(),
                                entry,
                                i,
                                positions[j],
                                positions[j + 1] - shapeSize,
                                dataSet.getValueTextColor(j / 2 + mXBounds.min));
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                c,
                                icon,
                                (int) (positions[j] + iconsOffset.x),
                                (int) (positions[j + 1] + iconsOffset.y),
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight()
                        );
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        ScatterData scatterData = mChart.getScatterData();

        for (Highlight high : indices) {

            IScatterDataSet set = scatterData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            final Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                    .getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }


}
