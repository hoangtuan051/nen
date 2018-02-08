package com.example.tonytuan.foodplace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.tonytuan.foodplace.Entity.FragmentFactory;

import java.util.Stack;

/**
 * Created by Tony Tuan on 01/24/2018.
 */

public class ParentActivity extends AppCompatActivity {

    public Stack<Fragment> fragmentStack;
    public FragmentManager fragmentManager;
    public int widthScreen;
    public int heightScreen;
    public FrameLayout frMain;

    public ParentActivity(int titleRes){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        fragmentStack = new Stack<Fragment>();
        fragmentManager = getSupportFragmentManager();
        Display display = getWindowManager().getDefaultDisplay();
        widthScreen = display.getWidth();
        heightScreen = display.getHeight();
    }

    public void replaceFragment(final int key){
        Fragment mFragment = FragmentFactory.getFragmentByKey(key);
        Fragment last = fragmentStack.lastElement();
        showFromRight(mFragment);
        hideFromRight(last);
    }

    private void showFromRight(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).show(fragment);
        ft.add(R.id.frMain, fragment);

        fragmentStack.lastElement().onPause();
        fragmentStack.push(fragment);
        ft.commit();
    }

    private void hideFromRight(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).hide(fragment);
        ft.commit();
    }

    public void startFragment(final int key){
        Fragment mFragment = FragmentFactory.getFragmentByKey(key);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.frMain, mFragment);
        fragmentStack.push(mFragment);
        ft.commit();
    }

    private void handleExit(){
        if(fragmentStack.size() > 0){
            Fragment show = fragmentStack.get(fragmentStack.size() - 2);
            Fragment remove = fragmentStack.get(fragmentStack.size() - 1);

            showFromLeft(show);
            hideFromLeft(remove);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fragmentStack.size() > 0)
            handleExit();
    }

    public void showFromLeft(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right).show(fragment);
        fragment.onResume();
        ft.commit();
    }

    public void hideFromLeft(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right).hide(fragment);
        fragment.onPause();
        fragmentStack.pop();
        ft.remove(fragment);
        ft.commit();
    }
}
