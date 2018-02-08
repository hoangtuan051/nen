package com.example.tonytuan.foodplace.Entity;

import android.support.v4.app.Fragment;

import com.example.tonytuan.foodplace.common.ConstantValue;
import com.example.tonytuan.foodplace.fragments.ReviewFragment;

/**
 * Created by Tony Tuan on 01/24/2018.
 */

public class FragmentFactory {
    public static Fragment getFragmentByKey(final int key){
        switch (key){
            case ConstantValue.REVIEW_FRAGMENT:
                return new ReviewFragment();
        }

        return null;
    }
}
