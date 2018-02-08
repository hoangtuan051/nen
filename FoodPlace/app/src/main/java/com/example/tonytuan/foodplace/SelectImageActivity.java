package com.example.tonytuan.foodplace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonytuan.foodplace.adapter.CustomImageAdapter;
import com.example.tonytuan.foodplace.common.Common;
import com.example.tonytuan.foodplace.common.ConstantValue;
import com.example.tonytuan.foodplace.model.Image;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Tony Tuan on 01/29/2018.
 */

public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener{
    private ProgressBar progressBar;
    private GridView gridView;
    private ArrayList<Image> images;
    private int countSelected;
    private ContentObserver contentObserver;
    private static Handler handler;
    private Thread thread;
    private final String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private HashMap<String, Image> listImageSelected = new HashMap<String, Image>();
    private RelativeLayout rlBarTop;
    private RelativeLayout rlBack;
    private TextView tvTitle;
    private FloatingActionButton fab;
    private TextView tvAdd;
    private ImageLoader imageLoader;
    private CustomImageAdapter customImageAdapter;
    private int limitImage = 1;
    public String photoFileName = "photo.jpg";
    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypreff";
    private boolean isChange = false;
    private int curPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_choose_layout);
        initView();
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        imageLoader = ImageLoader.getInstance();
        initImageLoader(this, imageLoader);
    }

    private void saveCheck(String key){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(ConstantValue.CHECKED_CAPTURE_PERMISSION, key);
        prefsEditor.commit();
    }

    private String getCheck(){
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if(sharedPreferences != null)
            return sharedPreferences.getString(ConstantValue.CHECKED_CAPTURE_PERMISSION, "");
        else
            return null;
    }

    private void initImageLoader(Context context, ImageLoader imageLoader){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        imageLoader.init(config);
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
    }

    private void sendData(){
        Common.images = getSelected();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_image_select);
        gridView = (GridView) findViewById(R.id.gv_image_choose);
        rlBarTop = (RelativeLayout) findViewById(R.id.rlBarTop);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvAdd = (TextView) findViewById(R.id.tvAdd);

        gridView.setOnItemClickListener(onItemClickListener);
        tvAdd.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        fab.setOnClickListener(this);

        customImageAdapter = new CustomImageAdapter(this, R.layout.item_grid_image);
        gridView.setAdapter(customImageAdapter);
        startQueryImage();
    }

    private void abortImageLoading(){
        if (thread == null) {
            return;
        }

        if (thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startQueryImage() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case ConstantValue.FETCH_STARTED:
                        progressBar.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;
                    case ConstantValue.FETCH_COMPLETED:
                        customImageAdapter.addListItems(images);
                        progressBar.setVisibility(View.INVISIBLE);
                        gridView.setVisibility(View.VISIBLE);

                        countSelected = msg.arg1;
                        if (countSelected > 0) {
                            tvTitle.setText(countSelected + " " + "đã chon");
                            rlBack.setVisibility(View.VISIBLE);
                        } else {
                            tvTitle.setText("Gallery");
                            tvAdd.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

        contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                startImageLoading();
            }
        };
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, contentObserver);
        startImageLoading();
    }

    private void startImageLoading() {
        abortImageLoading();
        ImageLoaderRunnable runnable = new ImageLoaderRunnable();
        thread = new Thread(runnable);
        thread.start();
    }

    private void captureImageResponse(){
        Uri takenPhotoUri = getPhotoUri(photoFileName);
        String sId = takenPhotoUri.getLastPathSegment();
        Long id = Long.parseLong(getNumberFromString(sId));
        Image imageAdd = new Image(id, photoFileName, takenPhotoUri.getPath(), false);

        images.add(0, imageAdd);
        customImageAdapter.insert(imageAdd, 0);
        gridView.smoothScrollToPosition(0);
    }

    private Uri getPhotoUri(String fileName){
        if(isExternalStorageAvailable()){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
//
                }
            }

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
            else
                return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                    new File(mediaStorageDir.getPath() + File.separator + fileName));
        }

        return null;
    }

    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private ArrayList<Image> getSelected(){
        ArrayList<Image> result = new ArrayList<>();
        if(images != null)
            for(int i = 0; i < images.size(); i++)
                if(images.get(i).isSelected)
                    result.add(images.get(i));
        return result;
    }

    private void toggleSelection(int position){

        images.get(position).isSelected = !images.get(position).isSelected;

        for(int i = 0; i < images.size(); i++){
            if(i != position){
                images.get(i).isSelected = false;
            }
            else {
                if (images.get(i).isSelected)
                    countSelected = 1;
                else countSelected = 0;
            }
        }

//        if (images.get(position).isSelected)
//            countSelected++;
//        else
//            countSelected--;

        customImageAdapter.notifyDataSetChanged();

        if (countSelected == 0)
            tvAdd.setVisibility(View.GONE);
        else
            tvAdd.setVisibility(View.VISIBLE);

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            toggleSelection(position);
        }
    };

    private static final int CAPTURE_IMAGE_CODE = 1000;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_CODE && resultCode == RESULT_OK){
            captureImageResponse();
            toggleSelection(0);
        }
    }

    private void requestCapturePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 106);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 106);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 106:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveCheck("true");
                    initCamera();
                }
                break;
            default:
                break;
        }
    }

    private String getTimestamp(){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp curTimestamp = new Timestamp(now.getTime());

        return String.valueOf(curTimestamp.getTime() / 1000L);
    }

    private String getNumberFromString(String text){
        String numberOnly = text.replaceAll("[^0-9]", "");
        if(numberOnly.equalsIgnoreCase(""))
            return "0";
        return numberOnly;
    }

    private void initCamera() {
        photoFileName = "photo_" + getTimestamp() + ".jpg";
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri(photoFileName));
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAPTURE_IMAGE_CODE);
    }

    private void deselectAll(){
        for(int i = 0; i < images.size(); i++){
            images.get(i).isSelected = false;
        }

        countSelected = 0;
        customImageAdapter.notifyDataSetChanged();
        tvAdd.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        try {
            abortImageLoading();

            handler.removeCallbacksAndMessages(null);
            handler = null;

            getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(getCheck()!= null)
                    if(!getCheck().equalsIgnoreCase("true"))
                        requestCapturePermission();
                    else
                        initCamera();
                break;
            case R.id.rlBack:
                if(countSelected > 0)
                    deselectAll();
                else
                    finish();
                break;
            case R.id.tvAdd:
                if(countSelected > limitImage)
                    Toast.makeText(getApplicationContext(), "Tối đa chọn 1 hình", Toast.LENGTH_SHORT).show();
                else {
                    sendData();
                    finish();
                }
                break;
        }
    }

    private class ImageLoaderRunnable implements Runnable{
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Message message;
            if(customImageAdapter == null){
                message = handler.obtainMessage();
                /*
                If the adapter is null, this is first time this activity's view is
                being shown, hence send FETCH_STARTED message to show progress bar
                while images are loaded from phone
                 */
                message.what = ConstantValue.FETCH_STARTED;
                message.sendToTarget();
            }

            if (Thread.interrupted()) {
                return;
            }

            File file;
            HashSet<Long> selectedImages = new HashSet<Long>();
            if (images != null) {
                Image image;
                for (int i = 0, l = images.size(); i < l; i++) {
                    image = images.get(i);
                    file = new File(image.path);
                    if (file.exists() && image.isSelected) {
                        selectedImages.add(image.id);
                    }
                }
            }

            Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                            null, null, MediaStore.Images.Media.DATE_ADDED);

            /*
            In case this runnable is executed to onChange calling startImageLoading,
            using countSelected variable can result in a race condition. To avoid that,
            tempCountSelected keeps track of number of selected images. On handling
            FETCH_COMPLETED message, countSelected is assigned value of tempCountSelected.
             */
            int tempCountSelected = 0;
            ArrayList<Image> temp = new ArrayList<Image>(cursor.getCount());

            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));

                    Image imageSelect = listImageSelected.get(path);
                    if (imageSelect != null) {
                        temp.add(new Image(id, name, path, true));
                        tempCountSelected++;
                    } else {
                        temp.add(new Image(id, name, path, false));
                    }
                } while (cursor.moveToPrevious());
            }
            cursor.close();

            if (images == null) {
                images = new ArrayList<Image>();
            }
            images.clear();
            images.addAll(temp);

            message = handler.obtainMessage();
            message.what = ConstantValue.FETCH_COMPLETED;
            message.arg1 = tempCountSelected;
            message.sendToTarget();

            Thread.interrupted();
        }
    }

}
