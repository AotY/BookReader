package com.xjtu.bookreader.util;

import com.xjtu.bookreader.app.BookReaderApplication;

/**
 * Created by LeonTao on 2018/1/4.
 */

public class StringResourceUtil {

    public static String getStringById(int id) {
        return BookReaderApplication.getInstance().getResources().getString(id);
    }
}
