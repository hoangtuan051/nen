package com.example.tonytuan.foodplace.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.ViewHolder.ImageViewHolder;
import com.example.tonytuan.foodplace.model.Review;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> listReview;

    public ReviewAdapter(Context mContext, List<Review> listReview) {
        this.mContext = mContext;
        this.listReview = listReview;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_review_layout, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        List<String> url = new ArrayList<>();
        url = listReview.get(position).getImageUrl();
        ImageAdapter itemlistAdapter = new ImageAdapter(url, mContext);
        holder.recyclerView.setAdapter(itemlistAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);
        Glide.with(mContext).load(listReview.get(position).getUser().getAvatar())
            .into(holder.iv_avatar);
        holder.tv_user_review.setText(listReview.get(position).getUser().getUsername());
        holder.tv_num_friend.setText(String.valueOf(listReview.get(position).getUser().getFriendTotal()));
        holder.tv_num_review.setText(String.valueOf(listReview.get(position).getUser().getReviewTotal()));
        holder.tv_num_tip.setText(String.valueOf(listReview.get(position).getUser().getCommentTotal()));
        holder.tv_rating.setText(String.valueOf(listReview.get(position).getRating()) + "/5.0");
        holder.tv_header_review.setText(listReview.get(position).getHeader());
        holder.tv_content_review.setText(listReview.get(position).getContent());
        holder.tv_datetime.setText(listReview.get(position).getTime());
        holder.ratingBar.setRating((float)listReview.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        CircleImageView iv_avatar;
        TextView tv_user_review, tv_num_friend, tv_num_review, tv_num_tip, tv_datetime, tv_rating, tv_header_review, tv_content_review;
        RatingBar ratingBar;
        ImageView ivArrow;
        RecyclerView recyclerView;

        public ReviewViewHolder(View view) {
            super(view);
            iv_avatar = (CircleImageView) view.findViewById(R.id.iv_user_image_review);
            tv_user_review = (TextView) view.findViewById(R.id.tv_user_name_review);
            tv_num_friend = (TextView) view.findViewById(R.id.tv_num_friend);
            tv_num_review = (TextView) view.findViewById(R.id.tv_amount_review);
            tv_num_tip = (TextView) view.findViewById(R.id.tv_amount_reward_review);
            tv_datetime = (TextView) view.findViewById(R.id.tv_time_review);
            tv_rating = (TextView) view.findViewById(R.id.tv_rating_review);
            tv_header_review = (TextView) view.findViewById(R.id.tv_header_review);
            tv_content_review = (TextView) view.findViewById(R.id.tv_content_review);
            ratingBar = (RatingBar) view.findViewById(R.id.rtb_rating_review);
            ivArrow = (ImageView) view.findViewById(R.id.iv_arrow_review);
            recyclerView = (RecyclerView) view.findViewById(R.id.rclv_highlight_image_review);
        }
    }
}
