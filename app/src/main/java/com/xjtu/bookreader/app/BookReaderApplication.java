package com.xjtu.bookreader.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;

import com.koolearn.klibrary.ui.android.library.ZLAndroidApplication;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.util.SharedPreferencesUtils;
import com.xjtu.bookreader.util.UserUtil;

import org.litepal.LitePal;

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

        LitePal.initialize(this);


        initTextSize();


        if (SharedPreferencesUtils.getBoolean("first_init", true)) {
            // 初始化数据
            initDBData();

            // 初始化用户数据
            initUserData();

            SharedPreferencesUtils.putBoolean("first_init", false);
        }


        initToast();
    }


    private void initUserData() {
        UserUtil.setUserId(1000023);
        UserUtil.setUserName("无法长大。");
        UserUtil.setUserAvatar("https://avatars1.githubusercontent.com/u/8146843?s=460&v=4");
    }

    /**
     * init data
     */
    private void initDBData() {
        final String path = Environment.getExternalStorageDirectory() + "/Download";
        BookOfShelf bookOfShelf1 = new BookOfShelf(10001, "芳华");
        bookOfShelf1.setDownloaded(true);
        bookOfShelf1.setBookPath(path + "/芳华.epub");
        bookOfShelf1.setCoverImage("https://img3.doubanio.com/lpic/s29418322.jpg");
        bookOfShelf1.save();

        BookOfShelf bookOfShelf2 = new BookOfShelf(10002, "步履不停");
        bookOfShelf2.setDownloaded(true);
        bookOfShelf2.setBookPath(path + "/步履不停.epub");
        bookOfShelf2.setCoverImage("http://mebook.cc/wp-content/uploads/2017/06/blob-39.png");
        bookOfShelf2.save();

        BookOfShelf bookOfShelf3 = new BookOfShelf(10003, "艺术的故事");
        bookOfShelf3.setDownloaded(true);
        bookOfShelf3.setBookPath(path + "/艺术的故事.epub");
        bookOfShelf3.setCoverImage("https://img3.doubanio.com/lpic/s3219163.jpg");
        bookOfShelf3.save();

        BookOfShelf bookOfShelf4 = new BookOfShelf(10004, "我的前半生");
        bookOfShelf4.setDownloaded(true);
        bookOfShelf4.setBookPath(path + "/我的前半生.epub");
        bookOfShelf4.setCoverImage("https://img1.doubanio.com/lpic/s2720819.jpg");
        bookOfShelf4.save();

        BookOfShelf bookOfShelf5 = new BookOfShelf(10005, "百年孤独");
        bookOfShelf5.setDownloaded(true);
        bookOfShelf5.setBookPath(path + "/百年孤独.epub");
        bookOfShelf5.setCoverImage("https://img3.doubanio.com/lpic/s6384944.jpg");
        bookOfShelf5.save();

        BookOfShelf bookOfShelf6 = new BookOfShelf(10006, "活着");
        bookOfShelf6.setDownloaded(true);
        bookOfShelf6.setBookPath(path + "/活着.epub");
        bookOfShelf6.setCoverImage("https://img3.doubanio.com/lpic/s27279654.jpg");
        bookOfShelf6.save();

        BookOfShelf bookOfShelf7 = new BookOfShelf(10007, "人间失格");
        bookOfShelf7.setDownloaded(true);
        bookOfShelf7.setBookPath(path + "/人间失格.epub");
        bookOfShelf7.setCoverImage("https://img3.doubanio.com/lpic/s6100756.jpg");
        bookOfShelf7.save();


        BookOfShelf bookOfShelf8 = new BookOfShelf(10008, "月亮与六便士");
        bookOfShelf8.setDownloaded(false);
        bookOfShelf8.setBookPath(path + "/月亮与六便士.epub");
        bookOfShelf8.setCoverImage("https://img1.doubanio.com/lpic/s2659208.jpg");
        bookOfShelf8.save();
    }


    private void initToast() {

//        Toasty.Config.getInstance().setErrorColor( @ColorInt int errorColor) // optional
//                .setInfoColor( @ColorInt int infoColor) // optional
//                .setSuccessColor( @ColorInt int successColor) // optional
//                .setWarningColor( @ColorInt int warningColor) // optional
//                .setTextColor( @ColorInt int textColor) // optional
//                .tintIcon( boolean tintIcon) // optional (apply textColor also to the icon)
//                .setToastTypeface(@NonNull Typeface typeface) // optional
//                .setTextSize( int sizeInSp) // optional
//                .apply(); // required
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
