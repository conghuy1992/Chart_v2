
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
    String TAG = "ScatterChartRenderer";
    protected ScatterDataProvider mChart;

    public ScatterChartRenderer(ScatterDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
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
    int maxWidth;

    protected void drawDataSet(Canvas c, IScatterDataSet dataSet) {
        Log.d(TAG, "drawDataSet");
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
            maxWidth = (int) (x2 - x1);

        }


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

//            mRenderPaint.setColor(dataSet.getColor(i / 2));
//            mRenderPaint.setColor(Color.GREEN);
//            renderer.renderShape(
//                    c, dataSet, mViewPortHandler,
//                    mPixelBuffer[0], mPixelBuffer[1],
//                    mRenderPaint);

            if(e.visible){
                renderer.renderShape(
                        c, dataSet, mViewPortHandler,
                        mPixelBuffer[0], mPixelBuffer[1],
                        mRenderPaint);
            }


        }
    }

    @Override
    public void drawValues(Canvas c) {
        Log.d(TAG, "drawValues");
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

                    if (entry.visible && entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        int y = (int) (positions[j + 1] + iconsOffset.y);
                        int w = icon.getIntrinsicWidth();
                        int h = icon.getIntrinsicHeight();
                        if (maxWidth > 0) {
                            w = maxWidth;
                            h = maxWidth;

//                            y-=h/2;
                        }

                        Utils.drawImage(
                                c,
                                icon,
                                (int) (positions[j] + iconsOffset.x),
                                y,
                                w,
                                h);
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
