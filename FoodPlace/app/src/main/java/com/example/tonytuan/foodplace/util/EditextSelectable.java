package com.example.tonytuan.foodplace.util;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by Tony Tuan on 01/11/2018.
 */

public class EditextSelectable extends android.support.v7.widget.AppCompatEditText {

    public interface onSelectionChangedListener {
        public void onSelectionChanged(int selStart, int selEnd);
    }

    private List<onSelectionChangedListener> listeners;

    public EditextSelectable(Context context) {
        super(context);
    }

    public EditextSelectable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditextSelectable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addOnSelectionChangedListener(onSelectionChangedListener o){
        listeners.add(o);
    }

    protected void onSelectionChanged(int selStart, int selEnd){
        for (onSelectionChangedListener l : listeners)
            l.onSelectionChanged(selStart, selEnd);

    }
}
