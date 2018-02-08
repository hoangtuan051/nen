package com.example.mylibrary;

import android.content.Context;

/**
 * Created by Tony Tuan on 01/30/2018.
 */

public class CompatUtils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
