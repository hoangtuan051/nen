package com.example.tonytuan.foodplace.interfaces;

/**
 * Created by Tony Tuan on 01/11/2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void onCoordinate(int curX, int curY);
}
