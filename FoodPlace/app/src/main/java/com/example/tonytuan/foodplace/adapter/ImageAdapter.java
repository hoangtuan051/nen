package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.ViewHolder.ImageViewHolder;

import java.util.List;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private List<String> listImage;
    private Context mContext;

    public ImageAdapter(List<String> listImage, Context mContext) {
        this.listImage = listImage;
        this.mContext = mContext;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_image_layout, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        Glide.with(mContext).load(listImage.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
}
