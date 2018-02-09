package com.example.tonytuan.foodplace;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonytuan.foodplace.Entity.AndroidMultiPartEntity;
import com.example.tonytuan.foodplace.adapter.ContentAdapter;
import com.example.tonytuan.foodplace.callback.ItemTouchHelperCallback;
import com.example.tonytuan.foodplace.common.Common;
import com.example.tonytuan.foodplace.common.ConstantValue;
import com.example.tonytuan.foodplace.model.Gallery;
import com.example.tonytuan.foodplace.model.Head;
import com.example.tonytuan.foodplace.model.Header;
import com.example.tonytuan.foodplace.model.Image;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private ImageView fab, iv_content;
    private RecyclerView recyclerView;
    private EditText edt_content, edt_description;
    private TextView tv_share, tv_back, tv_prb;
    private RelativeLayout rl_root, rl_back;
    private ProgressBar prb;
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
    private String imgPath = "";
    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    private int sizeEdit = 0;
    int a = 10;
    private boolean isClicked = false;
    long totalSize = 0;
    int count = 0;

    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View viewChild = rv.findChildViewUnder(e.getX(), e.getY());

            Log.d("position", rv.getChildAdapterPosition(viewChild) + "");

            if (viewChild instanceof EditText) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        EditText layout = (EditText) viewChild;
                        float x = e.getX();
                        float y = e.getY();
                        int offset = layout.getOffsetForPosition(x, y);
                        if (offset > 0) {
                            ((EditText) viewChild).setSelection(offset);
                            if (offset < layout.getText().length() - 1) {
                                pos = rv.getChildAdapterPosition(viewChild);
                                beforeStr = layout.getText().toString().substring(0, offset);
                                afterStr = layout.getText().toString().substring(offset, layout.getText().toString().length());
                                Log.d("pos", pos + " " + offset);
                            } else {
                                pos = -1;
                            }
                        }
                        break;
                    default:
                        ((EditText) viewChild).requestFocus();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        initViews();
    }

    private void saveCheck(String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(ConstantValue.CHECKED_LOAD_PERMISSION, key);
        prefsEditor.commit();
    }

    private String getCheck() {
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedPreferences != null)
            return sharedPreferences.getString(ConstantValue.CHECKED_LOAD_PERMISSION, "");
        else
            return null;
    }

    private void initViews() {
        fab = (ImageView) findViewById(R.id.fab);
        edt_content = (EditText) findViewById(R.id.edt_content);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_contents);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_share = (TextView) findViewById(R.id.tv_share);
        prb = (ProgressBar) findViewById(R.id.prb);
        tv_prb = (TextView) findViewById(R.id.tv_prb);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadToServer().execute(Common.images.get(0).path);
                //uploadToServer();
            }
        });

        edt_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                edt_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                sizeEdit = edt_content.getHeight() / 3;
            }
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogImage();
//            }
//        });

        edt_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        EditText layout = (EditText) v;
                        float x = event.getX() + edt_content.getScrollX();
                        float y = event.getY() + edt_content.getScrollY();
                        int offset = layout.getOffsetForPosition(x, y);
                        if (offset > 0)
                            edt_content.setSelection(offset);
                        break;
                    case MotionEvent.ACTION_UP:
                        edt_content.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_content, InputMethodManager.SHOW_IMPLICIT);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            }
                        }, 100);

                        Log.d("ma", "open");
                        break;
                }
                return true;
            }
        });

        rl_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rl_root.getWindowVisibleDisplayFrame(r);
                int screenHeight = rl_root.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                Log.d("mapi", "keypadHeight = " + keypadHeight);

                if(keypadHeight > screenHeight * 0.15){
                    //keyboard is opened
                    //Toast.makeText(getApplicationContext(), "opened", Toast.LENGTH_SHORT).show();
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
                            Log.d("type", "2");

                          recyclerView.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                              }
                          }, 100);
                    }
                }
                else{
                    //keyboard is closed
                    if(adapter.getItemCount() == 3)
                    {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(edt_content.getLayoutParams());
                        layoutParams.addRule(RelativeLayout.BELOW, R.id.rcv_contents);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                        edt_content.setLayoutParams(layoutParams);
                        edt_content.setVisibility(View.VISIBLE);
                        //edt_content.requestFocus();

                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(recyclerView.getLayoutParams());
                        layoutParams2.addRule(RelativeLayout.BELOW, R.id.rl_back);
                      //  layoutParams2.addRule(RelativeLayout.ABOVE, R.id.edt_content);
                        recyclerView.setLayoutParams(layoutParams2);
                        Log.d("type", "1");
                    }


                }
            }
        });

//        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (bottom < oldBottom) {
//                    Toast.makeText(getApplicationContext(), "resume " + isResume + "", Toast.LENGTH_SHORT).show();
//                    if (isResume) {
//                        recyclerView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
//                            }
//                        }, 100);
//
//                        isResume = false;
//                    }
//                }
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView1, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int offset = recyclerView1.computeVerticalScrollOffset();
                int extent = recyclerView1.computeVerticalScrollExtent();
                int range = recyclerView1.computeVerticalScrollRange();
                int percentage = (int) (100.0 * (offset) / (float) (range - extent));
                Log.d("items", offset + " " + sizeEdit + " " + extent + " " + range + " " + percentage + " " + dy);

                if (adapter.getItemCount() == 3) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(edt_content.getLayoutParams());
                    layoutParams.removeRule(RelativeLayout.BELOW);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
                    edt_content.setLayoutParams(layoutParams);
                    edt_content.setVisibility(View.VISIBLE);
                    edt_content.requestFocus();

                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(recyclerView1.getLayoutParams());
                    layoutParams2.addRule(RelativeLayout.BELOW, R.id.rl_back);
                    layoutParams2.addRule(RelativeLayout.ABOVE, R.id.edt_content);
                    recyclerView1.setLayoutParams(layoutParams2);

                    Log.d("type", "2");
                }

                if (dy < 0)
                    edt_content.setVisibility(View.GONE);
                else {
                    if (percentage > 99) {
                        // if(layoutManager.findLastVisibleItemPosition() + 1 == recyclerView.getAdapter().getItemCount()){
                        Log.d("testcoor", recyclerView.getChildAt(recyclerView.getChildCount() - 1).getMeasuredHeight() + " 3333");
                        edt_content.setVisibility(View.VISIBLE);
                        //recyclerView.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition());
                        //}
                    }
                }
            }
        });

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int initialTouchX = (int) event.getRawX();
                int initialTouchY = (int) event.getRawY();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                        xDelta = (int) v.getX() - initialTouchX;
                        yDelta = (int) v.getY() - initialTouchY;

                        Log.d("gestures", "down " + xDelta + " " + initialTouchX + " " + params.rightMargin);
                        //  Toast.makeText(getApplicationContext(), "down " + params.leftMargin + " " + params.topMargin, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if(initialTouchX > width - v.getWidth() + 50){
                            v.animate()
                                    .x(width - v.getWidth())
                                    .y(event.getRawY() + yDelta)
                                    .setDuration(0)
                                    .start();
                        }
                        else if(initialTouchX < v.getWidth() - 50){
                            v.animate()
                                    .x(0)
                                    .y(event.getRawY() + yDelta)
                                    .setDuration(0)
                                    .start();
                        }
                        else{
                            v.animate()
                                    .x(event.getRawX() + xDelta)
                                    .y(event.getRawY() + yDelta)
                                    .setDuration(0)
                                    .start();
                        }

                        Log.d("gestures", "move " + initialTouchX + " " + initialTouchY + " " + fab.getWidth());

                        break;
                    case MotionEvent.ACTION_UP:
                        if(initialTouchX < width/2){
                            if(initialTouchY < height / 8){
                                v.animate()
                                        .x(0)
                                        .y(height / 8)
                                        .setDuration(0)
                                        .start();
                            }
                            else if(initialTouchY > height * 7/8){
                                v.animate()
                                        .x(0)
                                        .y(6*height/8)
                                        .setDuration(0)
                                        .start();
                            }
                            else {
                                v.animate()
                                        .x(0)
                                        .y(event.getRawY() + yDelta)
                                        .setDuration(0)
                                        .start();
                            }
                        }
                        else{
                            if(initialTouchY < height / 8){
                                v.animate()
                                        .x(width - v.getWidth())
                                        .y(height / 8)
                                        .setDuration(0)
                                        .start();
                            }
                            else if(initialTouchY > height * 7/8){
                                v.animate()
                                        .x(width - v.getWidth())
                                        .y(6*height/8)
                                        .setDuration(0)
                                        .start();
                            }
                            else {
                                v.animate()
                                        .x(width - v.getWidth())
                                        .y(event.getRawY() + yDelta)
                                        .setDuration(0)
                                        .start();
                            }
                        }

                        int xDiff = (int) (event.getRawX() - initialTouchX);
                        int yDiff = (int) (event.getRawY() - initialTouchY);

                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD && (xDiff < 10 && yDiff < 10)) {
                            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                            showDialogImage();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        setAdapter();
    }

    private void uploadToServer(){
        File file = new File(imgPath);

        Ion.with(Main3Activity.this)
            .load("POST", Common.UPLOAD_IMAGE_URL)
            .uploadProgress(new ProgressCallback() {
                @Override
                public void onProgress(long uploaded, long total) {
                    Log.d("mapi", ">>>upload>>" + uploaded + "/" + total);
                }
            })
            .setMultipartFile("abc", "image/jpeg", file)
            .setMultipartParameter("name", "abc")
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if(e != null)
                        Toast.makeText(getApplicationContext(), "File upload error", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(), "File upload completed", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void requirePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 104:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        saveCheck("true");
                        uploadImage();
                    }
                    break;
            }
        }
    }

    private void setAdapter() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        contentList = new ArrayList<>();
        adapter = new ContentAdapter(this, contentList);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(onItemTouchListener);
        adapter.addItemContent(new Head());
    }

    private void uploadImage() {
        startActivity(new Intent(this, SelectImageActivity.class));
    }

    private void showDialogImage() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Choose a Image");
        dialog.setTitle("Add image!!!");
        dialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_image_layout, null);
        dialog.setView(view);
        edt_description = (EditText) view.findViewById(R.id.edt_note);
        iv_content = (ImageView) view.findViewById(R.id.iv_image);

        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCheck() != null)
                    if (!getCheck().equalsIgnoreCase("true"))
                        requirePermission();
                    else
                        uploadImage();
                else
                    requirePermission();
            }
        });

//        Glide.with(getBaseContext()).load("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-12691.png")
//                .into(iv_content);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gallery = new Gallery(imgPath, edt_description.getText().toString());
                if (pos != -1 && !adapter.isTextChange()) {
                    adapter.updateValueAtIndex(pos, new Header(beforeStr));
                    adapter.addItemAtIndex(pos + 1, gallery);
                    if (!afterStr.equalsIgnoreCase("")) {
                        adapter.addItemAtIndex(pos + 2, new Header(afterStr));
                        afterStr = "";
                    }
                    pos = -1;
                } else {
                    if (!edt_content.getText().toString().equalsIgnoreCase(""))
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

    private Bitmap getResizedBitmap(final Bitmap bm, int newHeight, int newWidth) {

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
        bm.recycle();
        return resizedBitmap;
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
            if (o.outHeight > max_size || o.outWidth > max_size) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
            Log.d("exxx", e.getMessage());
        }
        return b;
    }

    private String getPathImageFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                this,
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    private void getImage(Image image) {
        String exstorageDirectory = Environment.getExternalStorageState().toString();
        OutputStream outStream = null;
        int aspectRatio = 1;
        File file = new File(exstorageDirectory, "uploadTemp.jpg");
        if (file.exists()) {
            file.delete();
            file = new File(exstorageDirectory, "uploadTemp.jpg");
        }
        imgPath = image.path;
        Bitmap bitmap = BitmapFactory.decodeFile(image.path);
        aspectRatio = Math.max(bitmap.getWidth(), bitmap.getHeight()) / 1000;
        try {
            bitmap = getResizedBitmap(bitmap, (int) (bitmap.getWidth() / aspectRatio), (int) (bitmap.getHeight() / aspectRatio));
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        iv_content.setImageBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        try {
            if (Common.images.size() > 0) {
                ArrayList<Image> images = Common.images;
                //Common.images = new ArrayList<>();
                if (images.size() > 0)
                    getImage(images.get(0));
            }
        } catch (Exception e) {
            Common.images = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private class UploadToServer extends AsyncTask<String, Integer, String>{

        private final String TAG = UploadToServer.class.getName();
        private NotificationCompat.Builder mBuilder;
        private NotificationManager mNotificationManager;

        @Override
        protected void onPreExecute() {
            prb.setProgress(0);
            tv_prb.setText("0%");
            initNotification();
            setStartedNotification();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            Log.d("mapi", values[0] + "");
            prb.setVisibility(View.VISIBLE);
            tv_prb.setVisibility(View.VISIBLE);
            prb.setProgress(values[0]);
            tv_prb.setText(String.valueOf(values[0]) + "%");
            int icr = values[0].intValue();
//            if(icr == 0)
//                setProgressNotification();
            updateProgressNotification(icr);
        }

        @Override
        protected String doInBackground(String... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Common.UPLOAD_IMAGE_URL);

            try{
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float)totalSize) * 100));
                        Log.d("mapi", num + "");
                    }
                });

                File sourceFile = new File(Common.images.get(count).path);
                // Adding file data to http body
                entity.addPart("ab" + count, new FileBody(sourceFile));
                entity.addPart("name", new StringBody("ab" + count));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            }catch (ClientProtocolException e){
                responseString = e.toString();
            }
            catch (IOException e){
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "Response from server: " + result);
            // showing the server response in an alert dialog
           // showAlert(result);
            prb.setVisibility(View.GONE);
            tv_prb.setVisibility(View.GONE);
            setCompletedNotification();

            if(result != null){
                ++count;
                if(count < Common.images.size()) {
                    totalSize = 0;
                    new UploadToServer().execute(Common.images.get(count).path);
                }
                else {
                    Common.images = new ArrayList<>();
                    count = 0;
                }
            }
        }

        private void initNotification(){
            mNotificationManager = (NotificationManager) getBaseContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getBaseContext());
        }

        private void setStartedNotification(){
            mBuilder.setSmallIcon(R.drawable.chip)
                    .setContentTitle("DDAU upload service")
                    .setContentText("Started");

           // mBuilder.setContentIntent(resultPendingIntent);
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(Main3Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify(1, mBuilder.build());

        }

//        private void setProgressNotification() {
//            mBuilder.setContentTitle("DDAU upload service").setContentText("Downloading in progress ")
//                    .setSmallIcon(R.drawable.chip);
//        }

        private void updateProgressNotification(int icr){
            mBuilder.setContentTitle("DDAU upload service")
                    .setContentText("Downloading in progress " + icr + "%" + " - " + count + "/" + Common.images.size())
                    .setSmallIcon(R.drawable.chip)
                    .setProgress(100, icr, false);
            mNotificationManager.notify(1, mBuilder.build());
        }

        private void setCompletedNotification(){
            mBuilder.setSmallIcon(R.drawable.chip)
                    .setContentTitle("DDAU upload service")
                    .setContentText("Completed");

            PendingIntent pendingIntent = PendingIntent.getActivity(Main3Activity.this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify(1, mBuilder.build());
        }

        private void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
