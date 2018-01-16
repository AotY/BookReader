package com.xjtu.bookreader.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.book.Bookmark;

/**
 * Created by LeonTao on 2018/1/10.
 */

public class KooreaderUtil {


    /**
     * 跳转到阅读Activity，KooReader怎么返回数据呢？ startOnResult? 广播？ EventBus?
     *
     * @param data
     */
    public static void openBook(Activity activity, Book data, Bookmark bookmark) {
        // bookmark
        KooReader.openBookActivity(activity, data, bookmark);
        activity.overridePendingTransition(com.ninestars.android.R.anim.tran_fade_in, com.ninestars.android.R.anim.tran_fade_out);
    }


    /**
     * 需要返回结果
     *
     * @param activity
     * @param requestCode
     * @param data
     * @param bookmark
     */
    public static void openBookForResult(Activity activity, int requestCode, Book data, Bookmark bookmark) {
        // bookmark
        KooReader.openBookActivityForResult(activity, requestCode, data, bookmark);
        activity.overridePendingTransition(com.ninestars.android.R.anim.tran_fade_in, com.ninestars.android.R.anim.tran_fade_out);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
