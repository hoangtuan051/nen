package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.R;

import java.util.List;

/**
 * Created by Tony Tuan on 10/20/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> listUrl;

    public ViewPagerAdapter(Context mContext, List<String> listUrl) {
        this.mContext = mContext;
        this.listUrl = listUrl;
    }

    @Override
    public int getCount() {
        return listUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView ivItem;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_viewpager, container, false);

        ivItem = (ImageView) itemView.findViewById(R.id.iv_item);
        Glide.with(mContext).load(listUrl.get(position))
                .into(ivItem);
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout ) object);
    }
}
