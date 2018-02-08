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
import com.example.tonytuan.foodplace.model.Tip;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tony Tuan on 10/19/2017.
 */


public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder>{

    private Context mContext;
    private List<Tip> mList;

    public TipAdapter(Context mContext, List<Tip> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tip_layout, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getUser().getAvatar())
            .into(holder.iv_avatar);

        holder.tv_user_review.setText(mList.get(position).getUser().getName());
        holder.tv_num_friend.setText(mList.get(position).getUser().getFriendTotal());
        holder.tv_num_review.setText(mList.get(position).getUser().getReviewTotal());
        holder.tv_num_tip.setText(mList.get(position).getUser().getCommentTotal());
        holder.tv_datetime.setText(mList.get(position).getTime());
        holder.tv_content_tip.setText(mList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TipViewHolder extends RecyclerView.ViewHolder{

        CircleImageView iv_avatar;
        TextView tv_user_review, tv_num_friend, tv_num_tip, tv_num_review, tv_datetime, tv_content_tip;
        ImageView ivArrow;

        public TipViewHolder(View view) {
            super(view);
            iv_avatar = (CircleImageView) view.findViewById(R.id.iv_user_image_review);
            tv_user_review = (TextView) view.findViewById(R.id.tv_user_name_review);
            tv_num_friend = (TextView) view.findViewById(R.id.tv_num_friend);
            tv_num_review = (TextView) view.findViewById(R.id.tv_amount_review);
            tv_num_tip = (TextView) view.findViewById(R.id.tv_amount_reward_review);
            tv_datetime = (TextView) view.findViewById(R.id.tv_time_review);
            tv_content_tip = (TextView) view.findViewById(R.id.tv_content_review);;
            ivArrow = (ImageView) view.findViewById(R.id.iv_arrow_review);
        }
    }
}
