package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.ViewHolder.StoreViewHolder;
import com.example.tonytuan.foodplace.model.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Tuan on 10/18/2017.
 */



public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private Context mContext;
    private List<Store> listStore = new ArrayList<>();

    public StoreAdapter(Context mContext, List<Store> listStore) {
        this.mContext = mContext;
        this.listStore = listStore;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_store_layout, parent, false);
        return new StoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        holder.tvNameStore.setText(listStore.get(position).getName());
        holder.tvAddress.setText(listStore.get(position).getAddress());
        holder.tvSpecialFood.setText(listStore.get(position).getFood());
        holder.tvAmountReview.setText(String.valueOf(listStore.get(position).getReview()) + " reviews");
        holder.rb.setRating(listStore.get(position).getRating());
        Glide.with(mContext).load(listStore.get(position).getUrl())
                .into(holder.ivStore);
    }

    @Override
    public int getItemCount() {
        return listStore.size();
    }
}
