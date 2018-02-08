package com.example.tonytuan.foodplace;

import android.support.annotation.IntDef;
import android.widget.AbsListView;
import android.widget.NumberPicker;

/**
 * Created by Tony Tuan on 10/17/2017.
 */

public abstract class OnScrollObserver implements AbsListView.OnScrollListener {

    public abstract void onScrollUp();
    public abstract void onScrollDown();

    int last = 0;
    boolean control = true;

    @Override
    public void onScroll(AbsListView absListView, int current, int visibles, int total) {
        if (current < last && !control) {
            onScrollUp();
            control = true;
        } else if (current > last && control) {
            onScrollDown();
            control = false;
        }

        last = current;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }
}
