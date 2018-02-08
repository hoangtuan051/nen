package com.example.tonytuan.foodplace;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.FrameLayout;

import com.example.tonytuan.foodplace.common.ConstantValue;

public class ReviewFoodActivity extends ParentActivity {

    public ReviewFoodActivity() {
        super(R.string.app_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_food);

        initView();
    }

    private void initView() {
        this.frMain = (FrameLayout) findViewById(R.id.frMain);
        initData();
    }

    private void initData(){
        startFragment(ConstantValue.REVIEW_FRAGMENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 104:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, SelectImageActivity.class));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
