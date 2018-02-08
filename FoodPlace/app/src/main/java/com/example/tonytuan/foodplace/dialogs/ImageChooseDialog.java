package com.example.tonytuan.foodplace.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonytuan.foodplace.Main3Activity;
import com.example.tonytuan.foodplace.R;

/**
 * Created by Tony Tuan on 01/23/2018.
 */

public class ImageChooseDialog extends Dialog implements View.OnClickListener{

    private Main3Activity activity;
    private boolean fullScreen;
    private boolean dimBackgroud;
    private boolean titleBar;
    private ImageView iv_image;
    private EditText edt_title;
    private TextView tv_ok, tv_cancel;

    public ImageChooseDialog(Main3Activity activity, boolean fullScreen, boolean dimBackgroud, boolean titleBar) {
        super(activity);
        this.activity = activity;
        this.fullScreen = fullScreen;
        this.dimBackgroud = dimBackgroud;
        this.titleBar = titleBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_choose_dialog);
        findViews();
    }

    private void findViews(){
        iv_image = (ImageView) findViewById(R.id.iv_image);
        edt_title = (EditText) findViewById(R.id.edt_note);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        iv_image.setOnClickListener(this);
    }

    private void uploadImage(){

    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(
                (WindowManager.LayoutParams) params);
        if (!dimBackgroud)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (fullScreen)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_image:
                break;
            case R.id.tv_ok:
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
