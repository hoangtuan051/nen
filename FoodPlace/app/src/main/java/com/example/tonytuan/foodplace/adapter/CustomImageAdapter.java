package com.example.tonytuan.foodplace.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.model.Image;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Tuan on 01/25/2018.
 */

public class CustomImageAdapter extends ArrayAdapter<Image>{

    private Activity activity;
    private int layoutItem;
    private int width;
    private int height;
    public DisplayImageOptions optionsNomal;
    private int order = 0;

    public CustomImageAdapter(@NonNull Activity activity, @LayoutRes int resource) {
        super(activity, resource);
        this.activity = activity;
        this.layoutItem = resource;
        Display display = activity.getWindowManager().getDefaultDisplay();
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.optionsNomal = new DisplayImageOptions.Builder().showStubImage(R.drawable.photo_tran).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void addListItems(List<Image> list){
        for(Image image : list)
            add(image);
        notifyDataSetChanged();
    }

    public ArrayList<Image> getListItems(){
        ArrayList<Image> imageArrayList = new ArrayList<>();
        for(int i = 0; i < getCount(); i++){
            imageArrayList.add(getItem(i));
        }
        return imageArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(activity);
            convertView = inflater.inflate(layoutItem, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view_image_select);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Image item = getItem(position);
        holder.imageView.getLayoutParams().width = width / 3;
        holder.imageView.getLayoutParams().height = width / 3 + 50;
        holder.imageView.requestFocus();

        if (item.isSelected) {
            holder.imageView.setColorFilter(0x88000000);
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.clearColorFilter();
            holder.iv_select.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage("file://" + item.path, holder.imageView, optionsNomal);
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        ImageView iv_select;
    }

    public void setOrder(int order){
        this.order = order;
    }
}
