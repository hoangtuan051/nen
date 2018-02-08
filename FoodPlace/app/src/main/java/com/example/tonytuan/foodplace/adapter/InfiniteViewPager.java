package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Tony Tuan on 10/22/2017.
 */

public class InfiniteViewPager extends ViewPager {

    boolean fakePositionWasSet;
    InfinitePagerAdapter mAdapter;

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int toRealPosition(int position, int count){
        position = position - 1;
        if(position < 0)
            position += count;
        else
            position = position % count;

        return position;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        //mAdapter = new InfinitePagerAdapter(adapter);
        super.setAdapter(adapter);
        setCurrentItem(0, false);
    }

    @Override
    public int getCurrentItem() {
        if(getAdapter().getCount() == 0){
            return super.getCurrentItem();
        }
        int position = super.getCurrentItem();
        if(getAdapter() instanceof InfinitePagerAdapter){
            InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) getAdapter();
            return (position % infinitePagerAdapter.getRealCount());
        }
        else{
            return super.getCurrentItem();
        }
    }

    @Override
    public void setCurrentItem(int item) {
       // super.setCurrentItem(item, false);
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if(getAdapter().getCount() == 0){
            super.setCurrentItem(item, smoothScroll);
            return;
        }
        if(getAdapter() instanceof InfinitePagerAdapter){
            InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) getAdapter();
            int currentPositionInAdapter = super.getCurrentItem();
            //shift current start position
            if(!fakePositionWasSet){
                item += getOffsetAmount();
                fakePositionWasSet = true;
            }

            int currentPositionInRealAdapter = getCurrentItem();
            if(currentPositionInRealAdapter == 0 && item == infinitePagerAdapter.getRealCount() - 1){
                item = currentPositionInAdapter - 1;
            }
            else if(currentPositionInRealAdapter == infinitePagerAdapter.getRealCount() - 1 && item == 0){
                item = currentPositionInAdapter + 1;
            }
            else{
                item = currentPositionInAdapter + (item - currentPositionInRealAdapter);
            }
        }
        else{
            item = getOffsetAmount() + (item % getAdapter().getCount());
        }

        super.setCurrentItem(item, smoothScroll);
    }

    private int getOffsetAmount() {
        if(getAdapter() instanceof InfinitePagerAdapter){
            InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) getAdapter();
            return infinitePagerAdapter.getRealCount() * 100;
        }
        return 0;
    }
}
