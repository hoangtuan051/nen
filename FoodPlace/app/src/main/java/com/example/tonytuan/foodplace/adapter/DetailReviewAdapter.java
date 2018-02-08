package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.tonytuan.foodplace.model.Review;

import java.util.List;

/**
 * Created by Tony Tuan on 10/19/2017.
 */

public class DetailReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int IMAGE = 1;
    public static final int USER = 2;
    public List<String> listUrl;
    public List<Review> listReview;
    public Context mContext;

    public DetailReviewAdapter(List<String> listUrl, List<Review> listReview, Context mContext) {
        this.listUrl = listUrl;
        this.listReview = listReview;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }
}
