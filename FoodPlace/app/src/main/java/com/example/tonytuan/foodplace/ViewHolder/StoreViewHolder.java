package com.example.tonytuan.foodplace.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tonytuan.foodplace.R;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class StoreViewHolder extends RecyclerView.ViewHolder{

    public ImageView ivStore;
    public TextView tvNameStore, tvAmountReview, tvSpecialFood, tvAddress;
    public RatingBar rb;

    public StoreViewHolder(View itemView) {
        super(itemView);
        ivStore = (ImageView) itemView.findViewById(R.id.iv_image_item_store_layout);
        tvNameStore = (TextView) itemView.findViewById(R.id.tv_name_store_item_store_layout);
        tvAmountReview = (TextView) itemView.findViewById(R.id.tv_amount_review_item_store_layout);
        tvSpecialFood = (TextView) itemView.findViewById(R.id.tv_special_food_item_store_layout);
        tvAddress = (TextView) itemView.findViewById(R.id.tv_address_item_store_layout);
        rb = (RatingBar) itemView.findViewById(R.id.rtb_rating_item_store_layout);
    }

}
