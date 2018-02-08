package com.example.tonytuan.foodplace.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.interfaces.ItemTouchHelperAdapter;
import com.example.tonytuan.foodplace.interfaces.ItemTouchViewHolder;
import com.example.tonytuan.foodplace.model.Gallery;
import com.example.tonytuan.foodplace.model.Head;
import com.example.tonytuan.foodplace.model.Header;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tony Tuan on 01/11/2018.
 */

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter{

    private List<Object> objectList;
    private String deletedString = "", check = "";
    private final static int HEADER_TYPE = 1;
    private final static int CONTENT_TYPE = 2;
    private final static int IMAGE_TYPE = 3;
    private int pos = -1;
    private boolean isTextChange = false;
    private Activity context;

    public ContentAdapter(Activity context, List<Object> objectList){
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public int getItemViewType(int position) {
        if(objectList.get(position) instanceof Head)
            return HEADER_TYPE;
        else if(objectList.get(position) instanceof Header)
            return CONTENT_TYPE;
        else if(objectList.get(position) instanceof Gallery)
            return IMAGE_TYPE;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        switch (viewType){
            case HEADER_TYPE:
                View headerView = LayoutInflater.from(context).inflate(R.layout.row_header_layout, parent, false);
                viewHolder = new HeaderViewHolder(headerView);
                break;
            case CONTENT_TYPE:
                View contentView = LayoutInflater.from(context).inflate(R.layout.content_row, parent, false);
                viewHolder = new ContentViewHolder(contentView);
                break;
            case IMAGE_TYPE:
                View imageView = LayoutInflater.from(context).inflate(R.layout.row_content_layout, parent, false);
                viewHolder = new ImagesViewHolder(imageView);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = holder.getItemViewType();
        switch (viewType){
            case HEADER_TYPE:
                break;
            case CONTENT_TYPE:
                Header content = (Header) objectList.get(position);
                ((ContentViewHolder) holder).editText.setText(content.getTitle());

                ((ContentViewHolder) holder).editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        check = s.toString();
                        isTextChange = false;
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        deletedString = s.toString();
                        if(deletedString.equalsIgnoreCase(check))
                            isTextChange = false;
                        else
                            isTextChange = true;

                        if(pos != -1) {
                            if(objectList.get(pos) instanceof Header) {
                                ((Header) objectList.get(pos)).setTitle(deletedString);
                            }
                        }
//                        else
//                            ((Header) objectList.get(position)).setTitle(deletedString);

                        Log.d("string", ((ContentViewHolder) holder).editText.getSelectionStart() + "");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

//                ((ContentViewHolder) holder).editText.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        switch (event.getAction()){
//                            case MotionEvent.ACTION_DOWN:
//                                EditText layout = (EditText) v;
//                                float x = event.getX() + ((ContentViewHolder) holder).editText.getScrollX();
//                                float y = event.getY() + ((ContentViewHolder) holder).editText.getScrollY();
//                                int offset = layout.getOffsetForPosition(x, y);
//                                if(offset > 0){
//                                    ((ContentViewHolder) holder).editText.setSelection(offset);
//                                    if(offset < ((ContentViewHolder) holder).editText.toString().length() - 1) {
//                                        deletedString = ((ContentViewHolder) holder).editText.toString().substring(0, offset);
//                                        ((Header) objectList.get(position)).setTitle(cutString);
//                                        posItemTouch = position;
//                                    }
//                                }
//                                break;
//                            default:
//                                ((ContentViewHolder) holder).editText.requestFocus();
//                                break;
//                        }
//                        return true;
//                    }
//                });
                break;
            case IMAGE_TYPE:
                Gallery gallery = (Gallery) objectList.get(position);
              //  Bitmap bitmap = decodeFile(gallery.getUrl());
                Bitmap bmLoading = BitmapFactory.decodeResource(context.getResources(), R.drawable.im_loading);
                Glide.with(context).load(new File(gallery.getUrl()))
                        .placeholder(new BitmapDrawable(context.getResources(), bmLoading))
                        .centerCrop()
                        .into(((ImagesViewHolder) holder).iv);
                ((ImagesViewHolder) holder).tvNote.setText(gallery.getDescription());
                break;
        }
    }

    public boolean isTextChange() {
        return isTextChange;
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(objectList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(objectList, i, i - 1);
            }
        }

        if (objectList.get(fromPosition) instanceof Header){
            pos = fromPosition;
        }
        else
            pos = toPosition;

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onCoordinate(int curX, int curY) {

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder{
        private EditText editText;

        public ContentViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.edt_content);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder{
        private TextView tvNote;
        private ImageView iv;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tvNote = (TextView) itemView.findViewById(R.id.tv_note);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addItemContent(Object object){
        objectList.add(object);
        notifyDataSetChanged();
    }

    public void addItemAtIndex(int index, Object object){
        objectList.add(index, object);
        notifyDataSetChanged();
    }

    public void updateValueAtIndex(int index, Object object){
        objectList.set(index, object);
        notifyDataSetChanged();
    }
}
