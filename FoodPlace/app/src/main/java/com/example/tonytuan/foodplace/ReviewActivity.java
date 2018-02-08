package com.example.tonytuan.foodplace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.adapter.ContentAdapter;
import com.example.tonytuan.foodplace.callback.ItemTouchHelperCallback;
import com.example.tonytuan.foodplace.model.Gallery;
import com.example.tonytuan.foodplace.model.Header;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EditText edt_content;
    private NestedScrollView scroll;
    private ContentAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Object> contentList;
    private Gallery gallery;
    private int pos = -1;
    private String beforeStr = "", afterStr = "";
    private int xDelta;
    private int yDelta;
    private int position = 3;


    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View viewChild = rv.findChildViewUnder(e.getX(), e.getY());
            if(viewChild instanceof EditText){
                Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
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
                                Log.d("pos", pos + "");
                            }
                            else{
                                pos = -1;
                            }
                        }
                        break;
                    default:
                        //  viewChild.requestFocus();
                        break;
                }
            }
            else{
                int x = (int ) e.getX();
                int y = (int) e.getY();
                switch (e.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
//                        RecyclerView.LayoutParams lParams = (RecyclerView.LayoutParams) viewChild.getLayoutParams();
//                        xDelta = x - lParams.leftMargin;
//                        yDelta = y - lParams.topMargin;
                        Toast.makeText(getApplicationContext(), "down", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewChild.getLayoutParams();
//                        layoutParams.leftMargin = x - xDelta;
//                        layoutParams.topMargin = y - yDelta;
//                        layoutParams.rightMargin = 0;
//                        layoutParams.bottomMargin = 0;
                        int test = y - yDelta;
                        int pos = layoutManager.findLastVisibleItemPosition();
                      //  int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        Toast.makeText(getApplicationContext(), "move " + pos + " " + test, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT).show();
                        yDelta = -1;
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
        setContentView(R.layout.activity_review);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        edt_content = (EditText) findViewById(R.id.edt_content);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_contents);
        scroll = (NestedScrollView) findViewById(R.id.scroll);

       // setAdapter();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null)
                    showDialogImage();
            }
        });
        edt_content.requestFocus();
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
                    default:
                        edt_content.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_content, InputMethodManager.SHOW_IMPLICIT);
                        break;
                }
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(onItemTouchListener);

        //scroll.setNestedScrollingEnabled(false);


        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int cr = scroll.getScrollY();
                Log.d("cor", cr + "");
            }
        });
//
//
//        scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//            }
//        });
    }

    private void setAdapter(){
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        contentList = new ArrayList<>();
       // adapter = new ContentAdapter(this, contentList);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    private void showDialogImage() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Choose a Image");
        dialog.setTitle("Add image!!!");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.row_image_layout, null);
        dialog.setView(view);
        final EditText editText = view.findViewById(R.id.edt_note);
        final ImageView iv = view.findViewById(R.id.iv_image);
        // https://static.asiachan.com/Jeon.Somi.full.151082.jpg
        Glide.with(getBaseContext()).load("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-12691.png")
                .into(iv);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gallery = new Gallery("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-12691.png", editText.getText().toString());
                if(pos != -1 && !adapter.isTextChange()){
                    adapter.updateValueAtIndex(pos, new Header(beforeStr));
                    adapter.addItemAtIndex(pos+1, gallery);
                    if(!afterStr.equalsIgnoreCase(""))
                        adapter.addItemAtIndex(pos+2, new Header(afterStr));
                }
                else {
                    adapter.addItemContent(new Header(edt_content.getText().toString()));
                    adapter.addItemContent(gallery);
                }
                edt_content.setText("");
                edt_content.requestFocus();
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
}
