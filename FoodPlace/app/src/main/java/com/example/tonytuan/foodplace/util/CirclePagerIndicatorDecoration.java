package com.example.tonytuan.foodplace.util;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.bumptech.glide.load.engine.Resource;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class CirclePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;
    private final int mIndicatorHeight = (int) (DP * 16);//height of the space the indicator takes up at the bottom of the view
    private final float mIndicatorStrokeWith = DP * 2;
    private final float mIndicatorItemLength = DP * 16;
    private final float mIndicatorItemPadding = DP;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final Paint mPaint = new Paint();

    public CirclePagerIndicatorDecoration() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWith);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        //center horizontal, cal with and subtract from the center
        float totalLength = mIndicatorItemLength * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        //center vertical
        float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;
        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);

        //find active page
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        //find the offset of active page
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();
        float progress = mInterpolator.getInterpolation(left * (-1) / (float) width); //swipe
        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY, int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(Color.RED);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        if (progress == 0F) {
            // no swipe, draw a normal indicator
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;

            c.drawCircle(highlightStart + mIndicatorItemLength, indicatorPosY, itemWidth / 6, mPaint);

        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            // calculate partial highlight
            float partialLength = mIndicatorItemLength * progress;
            c.drawCircle(highlightStart + mIndicatorItemLength + partialLength, indicatorPosY, itemWidth / 6, mPaint);

            if(highlightPosition < itemCount - 1){
                highlightStart += itemWidth;
                c.drawCircle(highlightStart + partialLength, indicatorPosY, itemWidth / 6, mPaint);
            }
        }
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(Color.GRAY);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            // draw the circle for every item
            c.drawCircle(start + mIndicatorItemLength,indicatorPosY,itemWidth/6,mPaint);
            start += itemWidth;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }
}
