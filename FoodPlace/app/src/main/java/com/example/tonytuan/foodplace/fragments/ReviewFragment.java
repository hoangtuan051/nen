package com.example.tonytuan.foodplace.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.ParentActivity;
import com.example.tonytuan.foodplace.R;
import com.example.tonytuan.foodplace.adapter.ContentAdapter;
import com.example.tonytuan.foodplace.callback.ItemTouchHelperCallback;
import com.example.tonytuan.foodplace.model.Gallery;
import com.example.tonytuan.foodplace.model.Head;
import com.example.tonytuan.foodplace.model.Header;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Tony Tuan on 01/24/2018.
 */

public class ReviewFragment extends Fragment {

    ParentActivity activity;
    private FloatingActionButton fab1;
    private RelativeLayout rl_back;
    private ImageView fab, iv_content;
    private RecyclerView recyclerView;
    private EditText edt_content, edt_description;
    private ContentAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Object> contentList;
    private Gallery gallery;
    private int pos = -1;
    private String beforeStr = "", afterStr = "";
    private boolean isResume = false;
    private int xDelta, yDelta;
    private long lastTouchDown;
    private static int CLICK_ACTION_THRESHHOLD = 150;
    private final static int UPLOAD_IMAGE_CODE = 1;
    private String imgPath = "";
    private Bitmap bitmap;

    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View viewChild = rv.findChildViewUnder(e.getX(), e.getY());

            if(viewChild instanceof EditText){
                switch (e.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText layout = (EditText) viewChild;
                        float x = e.getX();
                        float y = e.getY();
                        int offset = layout.getOffsetForPosition(x, y);
                        if(offset > 0) {
                            ((EditText) viewChild).setSelection(offset);
                            if(offset < layout.getText().length() - 1){
                                pos = rv.getChildAdapterPosition(viewChild);
                                beforeStr = layout.getText().toString().substring(0, offset);
                                afterStr = layout.getText().toString().substring(offset, layout.getText().toString().length());
                                Log.d("pos", pos + " " + offset);
                            }
                            else{
                                pos = -1;
                            }
                        }
                        break;
                    default:
                        viewChild.requestFocus();
                        break;
                }
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);
        activity = (ParentActivity) getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        fab = (ImageView) view.findViewById(R.id.fab);
        edt_content = (EditText) view.findViewById(R.id.edt_content);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_contents);
        rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogImage();
//            }
//        });

        edt_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText layout = (EditText) v;
                        float x = event.getX() + edt_content.getScrollX();
                        float y = event.getY() + edt_content.getScrollY();
                        int offset = layout.getOffsetForPosition(x, y);
                        if(offset>0)
                            edt_content.setSelection(offset);
                        break;
                    case MotionEvent.ACTION_UP:
                        edt_content.requestFocus();
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_content, InputMethodManager.SHOW_IMPLICIT);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            }
                        }, 100);
                        break;
                }
                return true;
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    Toast.makeText(activity.getApplicationContext(), isResume + "", Toast.LENGTH_SHORT).show();
                    if(isResume) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            }
                        }, 100);

                        isResume = false;
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int offset = recyclerView.computeVerticalScrollOffset();
                int extent = recyclerView.computeVerticalScrollExtent();
                int range = recyclerView.computeVerticalScrollRange();
                int percentage = (int)(100.0 * offset / (float)(range - extent));
                Log.d("items", offset + " " + extent + " " + range + " " + percentage + " " + dy);

                if(adapter.getItemCount() == 3){
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(edt_content.getLayoutParams());
                    layoutParams.removeRule(RelativeLayout.BELOW);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
                    edt_content.setLayoutParams(layoutParams);
                    edt_content.setVisibility(View.VISIBLE);
                    edt_content.requestFocus();

                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(recyclerView.getLayoutParams());
                    layoutParams2.addRule(RelativeLayout.BELOW, R.id.rl_back);
                    layoutParams2.addRule(RelativeLayout.ABOVE, R.id.edt_content);
                    recyclerView.setLayoutParams(layoutParams2);
                }

                if(dy < 0)
                    edt_content.setVisibility(View.GONE);
                else {
                    if(percentage > 90 && percentage < 98){
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                    else if (percentage > 99) {
                        edt_content.setVisibility(View.VISIBLE);
                        edt_content.requestFocus();
                    }
                }
            }
        });

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int initialTouchX = (int) event.getRawX();
                int initialTouchY = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                        xDelta = (int)v.getX() - initialTouchX;
                        yDelta = (int)v.getY() - initialTouchY;

                        Log.d("gestures", "down " + xDelta + " " + initialTouchX + " " + params.rightMargin);
                        //  Toast.makeText(getApplicationContext(), "down " + params.leftMargin + " " + params.topMargin, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        v.animate()
                                .x(event.getRawX() + xDelta)
                                .y(event.getRawY() + yDelta)
                                .setDuration(0)
                                .start();
                        Log.d("gestures", "move " + initialTouchX + " " + initialTouchY + " " + fab.getWidth());

                        break;
                    case MotionEvent.ACTION_UP:
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int width = displayMetrics.widthPixels;

                        if(initialTouchX >  width - v.getWidth() + 50)
                        {
                            v.animate()
                                    .x(event.getRawX() - v.getWidth() - 25)
                                    .y(event.getRawY() + yDelta)
                                    .setDuration(0)
                                    .start();
                        }

                        int xDiff = (int) (event.getRawX() - initialTouchX);
                        int yDiff = (int) (event.getRawY() - initialTouchY);

                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD &&  (xDiff < 10 && yDiff < 10))  {
                            Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show();
                            showDialogImage();
                            return true;
                        }
//                        DisplayMetrics displayMetrics = new DisplayMetrics();
//                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                        int width = displayMetrics.widthPixels;
//                        RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) fab.getLayoutParams();
//                        layoutParam.leftMargin = width - initialTouchX - 2 * fab.getWidth();
//                        layoutParam.topMargin = initialTouchY - yDelta;
//                        fab.setLayoutParams(layoutParam);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        setAdapter();
    }

    private void requireGalleryPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 104);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 104);
            }
        }
    }


    private void setAdapter(){
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        contentList = new ArrayList<>();
        adapter = new ContentAdapter(activity, contentList);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(onItemTouchListener);
        adapter.addItemContent(new Head());
    }


    private void showDialogImage() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage("Choose a Image");
        dialog.setTitle("Add image!!!");
        dialog.setCancelable(false);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_image_layout, null);
        dialog.setView(view);
        edt_description = (EditText) view.findViewById(R.id.edt_note);
        iv_content = (ImageView) view.findViewById(R.id.iv_image);

        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireGalleryPermission();
                //startActivity(new Intent(activity, SelectImageActivity.class));
                //uploadImage();
            }
        });

//        Glide.with(getBaseContext()).load("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-12691.png")
//                .into(iv_content);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gallery = new Gallery(imgPath, edt_description.getText().toString());
                if(pos != -1 && !adapter.isTextChange()){
                    adapter.updateValueAtIndex(pos, new Header(beforeStr));
                    adapter.addItemAtIndex(pos+1, gallery);
                    if(!afterStr.equalsIgnoreCase("")) {
                        adapter.addItemAtIndex(pos+2, new Header(afterStr));
                        afterStr = "";
                    }
                    pos = -1;
                }
                else {
                    if(!edt_content.getText().toString().equalsIgnoreCase(""))
                        adapter.addItemContent(new Header(edt_content.getText().toString()));
                    adapter.addItemContent(gallery);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
                edt_content.setText("");
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Bitmap getResizedBitmap(final Bitmap bm, final int newHeight, final int newWidth) {

        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPLOAD_IMAGE_CODE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();

//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            imgPath = getPathImageFromUri(selectedImage);
            //Bitmap bitmap = decodeFile(imgPath);
            Glide.with(activity.getBaseContext()).load(new File(imgPath))
                    .into(iv_content);
            //  iv_content.setImageBitmap(bitmap);
        }
    }

    private Bitmap decodeFile(String imgPath) {
        Bitmap b = null;
        int max_size = 1000;
        File f = new File(imgPath);
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > max_size || o.outWidth > max_size)
            {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        }
        catch (Exception e)
        {
            Log.d("exxx", e.getMessage());
        }
        return b;
    }

    private String getPathImageFromUri(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                activity,
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }
}
