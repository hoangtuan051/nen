package com.example.tonytuan.foodplace.common;

import com.example.tonytuan.foodplace.model.Image;

import java.util.ArrayList;

/**
 * Created by Tony Tuan on 10/20/2017.
 */

public class Common {
    public static final String url = "http://maps.google.com/maps/api/staticmap?&zoom=18&size=600x300&maptype=roadmap&markers=color:green%7Clabel:G%7C";
    public static final String key = "&key=AIzaSyDkTWRmvSWUZ8GWmiae63Jfrj0SLFJ8J2c";
    public static ArrayList<Image> images = new ArrayList<>();
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static final String UPLOAD_IMAGE_URL = "http://163.44.207.128:8005/upload";
}
