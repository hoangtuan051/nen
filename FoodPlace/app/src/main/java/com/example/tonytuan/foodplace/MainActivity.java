package com.example.tonytuan.foodplace;

import android.animation.ValueAnimator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonytuan.foodplace.adapter.InfinitePagerAdapter;
import com.example.tonytuan.foodplace.adapter.InfiniteViewPager;
import com.example.tonytuan.foodplace.adapter.ReviewAdapter;
import com.example.tonytuan.foodplace.adapter.StoreAdapter;
import com.example.tonytuan.foodplace.adapter.TipAdapter;
import com.example.tonytuan.foodplace.adapter.ViewPagerAdapter;
import com.example.tonytuan.foodplace.common.Common;
import com.example.tonytuan.foodplace.model.DetailStore;
import com.example.tonytuan.foodplace.model.Item;
import com.example.tonytuan.foodplace.model.Review;
import com.example.tonytuan.foodplace.model.Store;
import com.example.tonytuan.foodplace.model.Tip;
import com.example.tonytuan.foodplace.model.User;
import com.example.tonytuan.foodplace.util.CirclePagerIndicatorDecoration;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout containerNavigation;
    private ScrollView scrollView;
    private RecyclerView rclvStore, rclvTip;
    private RecyclerView.LayoutManager layoutManager;
    private StoreAdapter adapter;
    private RatingBar rtbAvgRatingStore;
    private RecyclerView rclvReview;
    private ReviewAdapter reviewAdapter;
    private TipAdapter tipAdapter;
    private List<Store> listStore = new ArrayList<>();
    private List<Review> listReview = new ArrayList<>();
    private List<Tip> listTip = new ArrayList<>();
    private ImageView map;
    private InfiniteViewPager vpgImageUserItem;
    private PagerAdapter viewPagerAdapter;
    private List<String> url = new ArrayList<>();
    private TextView tv_num_page, tvPrice, tvAddress, tvNumReview, tvNumChecking, tvNumTip, tvStoreName, tvDescription, tvDistance, tvStart1Total, tvStart2Total, tvStart3Total, tvStart4Total, tvStart5Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        containerNavigation = (LinearLayout) findViewById(R.id.container_navigation);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        rclvStore = (RecyclerView) findViewById(R.id.rclv_the_same_place);
        rclvReview = (RecyclerView) findViewById(R.id.rclv_user_review);
        map = (ImageView) findViewById(R.id.iv_map);
        vpgImageUserItem = (InfiniteViewPager) findViewById(R.id.vpg_food_special_images);
        tvAddress = (TextView) findViewById(R.id.tv_address_store);
        tvDescription = (TextView) findViewById(R.id.tv_food_name);
        tvStoreName = (TextView) findViewById(R.id.tv_name_store);
        tvPrice = (TextView) findViewById(R.id.tv_money);
        tvNumReview = (TextView) findViewById(R.id.tv_amount_review);
        tvNumChecking = (TextView) findViewById(R.id.tv_amount_checking);
        tvNumTip = (TextView) findViewById(R.id.tv_amount_tip);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        tvStart1Total = (TextView) findViewById(R.id.tv_excellent_amount);
        tvStart2Total = (TextView) findViewById(R.id.tv_good_amount);
        tvStart3Total = (TextView) findViewById(R.id.tv_quite_amount);
        tvStart4Total = (TextView) findViewById(R.id.tv_so_amount);
        tvStart5Total = (TextView) findViewById(R.id.tv_bad_amount);
        tv_num_page = (TextView) findViewById(R.id.tv_num_page);
        rtbAvgRatingStore = (RatingBar) findViewById(R.id.rb_store);
        //parse json to model
        DetailStore mDataStore = new Gson().fromJson(loadJSONFromAsset(), DetailStore.class);
        for(int i = 0; i < mDataStore.getItems().size(); i++){
            url.add(mDataStore.getItems().get(i).getMyImageLink());
        }

        viewPagerAdapter = new InfinitePagerAdapter(new ViewPagerAdapter(this, url));
        vpgImageUserItem.setAdapter(viewPagerAdapter);
        vpgImageUserItem.setCurrentItem(0);
        vpgImageUserItem.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_num_page.setText("" + ((position % url.size()) + 1) + "/" + url.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tvDistance.setText(String.valueOf(Math.floor(mDataStore.getDistance() * 100) / 100) + " km");
        tvStoreName.setText(mDataStore.getName());
        tvAddress.setText(mDataStore.getFullAddress());
        tvDescription.setText(mDataStore.getFoodKind());
        tvNumReview.setText(String.valueOf(mDataStore.getReviewTotal()));
        tvNumChecking.setText(String.valueOf(mDataStore.getCheckinTotal()));
        tvNumTip.setText(String.valueOf(mDataStore.getTipTotal()));
        tvPrice.setText(String.valueOf(mDataStore.getMinPrice()) + "vnđ - " + String.valueOf(mDataStore.getMaxPrice()) + "vnđ");
        tvStart1Total.setText(String.valueOf(mDataStore.getStar5Total()));
        tvStart2Total.setText(String.valueOf(mDataStore.getStar4Total()));
        tvStart3Total.setText(String.valueOf(mDataStore.getStar3Total()));
        tvStart4Total.setText(String.valueOf(mDataStore.getStar2Total()));
        tvStart5Total.setText(String.valueOf(mDataStore.getStar1Total()));
        rtbAvgRatingStore.setRating(mDataStore.getAvgRating());
        Glide.with(this).load(Common.url + mDataStore.getLat() + "," + mDataStore.getLng() + Common.key)
                .into(map);

        for(int i = 0; i < 6; i++)
            listStore.add(new Store("http://cdn01.diadiemanuong.com/ddau/640x/a-hoai-spaghetti-84f9022636096693504325952.jpg", "Mì sườn bò 459", "34/2 Nguyễn Duy", i, i/2, "Mì"));
        adapter = new StoreAdapter(this, listStore);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rclvStore.setHasFixedSize(true);
        rclvStore.setLayoutManager(layoutManager);
        rclvStore.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rclvStore);
        rclvStore.addItemDecoration(new CirclePagerIndicatorDecoration());

        for(int i = 0; i < 2; i++){
            listReview.add(new Review(new User("1", "EchCon", "Nam", 80, 3, 9, "http://cdn01.diadiemanuong.com/ddau/640x/-45bd968d636059047184697703.jpg"),
                    "Mì ý chú Hoài vs món mới",
                    "Nghe nói quán mỳ nổi tiếng của chú Hoài dời qua địa chỉ mới rồi nhen, chỗ rộng hơn có Nghe nói quán mỳ nổi tiếng của chú Hoài dời qua địa chỉ mới rồi nhen, chỗ rộng hơn có Nghe nói quán mỳ nổi tiếng của chú Hoài dời qua địa chỉ mới rồi nhen, chỗ rộng hơn có",
                    "09/12/2016", 4.1, url
                    ));
        }

        reviewAdapter = new ReviewAdapter(this, listReview);
        rclvReview.setAdapter(reviewAdapter);
        rclvReview.setHasFixedSize(true);
        rclvReview.setNestedScrollingEnabled(false);
        rclvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rclvTip = (RecyclerView) findViewById(R.id.rclv_user_tip);
        tipAdapter = new TipAdapter(this, listTip);
        rclvTip.setAdapter(tipAdapter);
        rclvTip.setHasFixedSize(true);
        rclvTip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        containerNavigation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            private float mInitialX;
//            private float mInitialY;
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
//                {
//                    Toast.makeText(getBaseContext(),"you have clicked down Touch button",Toast.LENGTH_SHORT).show();
//                }
//                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
//                    Toast.makeText(getBaseContext(),"you have clicked up Touch button",Toast.LENGTH_SHORT).show();
//                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
//                    Toast.makeText(getBaseContext(),"you have clicked move Touch button",Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            private float mInitialY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mInitialY = motionEvent.getY();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    final float y = motionEvent.getY();
                    final float yDiff = mInitialY - y;
                  //  currentNavigation = containerNavigation.getHeight();
                    if(yDiff > 0.0){
                        Toast.makeText(getBaseContext(),"you have clicked down Touch button",Toast.LENGTH_SHORT).show();
//                        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//                        containerNavigation.setAnimation(slide_down);
//                        containerNavigation.setVisibility(View.GONE);

                        collapse(containerNavigation, 1000, 0);

//                        containerNavigation.animate()
//                                .translationYBy(containerNavigation.getHeight())
//                                .translationY(0)
//                                .alpha(0.0f)
//                                .setDuration(1000)
//                                .setListener(new AnimatorListenerAdapter() {
//                                    @Override
//                                    public void onAnimationEnd(Animator animation) {
//                                        super.onAnimationEnd(animation);
//                                        containerNavigation.setVisibility(View.GONE);
//                                    }
//                                });
                    }
                    else if(yDiff < 0.0){
//                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                        Toast.makeText(getBaseContext(),"you have clicked up Touch button",Toast.LENGTH_SHORT).show();
//                        containerNavigation.setAnimation(slide_up);
//                        containerNavigation.setVisibility(View.VISIBLE);
                        expand(containerNavigation, 1000, 168);
//                        containerNavigation.animate()
//                                .translationYBy(0)
//                                .translationY(containerNavigation.getHeight())
//                                .alpha(1.0f)
//                                .setDuration(1000)
//                                .setListener(new AnimatorListenerAdapter() {
//                                    @Override
//                                    public void onAnimationStart(Animator animation) {
//                                        super.onAnimationStart(animation);
//                                        containerNavigation.setVisibility(View.VISIBLE);
//                                        containerNavigation.setAlpha(0.0f);
//                                    }
//                                });
                    }
                }
                return false;
            }
        });
    }

    private void expand(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    private void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    private String loadJSONFromAsset(){
        String json = null;
        try{
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }

//    public static Bitmap getGoogleMapThumbnail(double lati, double longi){
//        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
//        Bitmap bmp = null;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpGet request = new HttpGet(URL);
//
//        InputStream in = null;
//        try {
//            in = httpclient.execute(request).getEntity().getContent();
//            bmp = BitmapFactory.decodeStream(in);
//            in.close();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return bmp;
//    }
}
