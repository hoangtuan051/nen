package com.example.tonytuan.foodplace.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.tonytuan.foodplace.R;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.iv_item);
    }
}
