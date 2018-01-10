package com.xjtu.bookreader.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;

import com.koolearn.klibrary.ui.android.library.ZLAndroidApplication;
import com.xjtu.bookreader.db.BookDBHelper;
import com.xjtu.bookreader.util.Logger;

/**
 * Created by LeonTao on 2017/12/30.
 */

public class BookReaderApplication extends ZLAndroidApplication {

    private static BookReaderApplication bookReaderApplication;

    public static BookReaderApplication getInstance() {
        return bookReaderApplication;
    }

//    private BookDBHelper bookDBHelper;

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        bookReaderApplication = this;
//        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);
        initTextSize();

        // 初始化数据
//        initData();
    }

    /**
     * 使其系统更改字体大小无效
     * 应用内手动调整字体大小
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }




}
