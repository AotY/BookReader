package com.xjtu.bookreader.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.support.annotation.ColorInt;

import com.koolearn.klibrary.ui.android.library.ZLAndroidApplication;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.db.BookDBHelper;
import com.xjtu.bookreader.util.Logger;

import org.litepal.LitePal;

import es.dmoral.toasty.Toasty;

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

        // 初始化数据
//        initData();

        initToast();
    }

    /**
     * init data
     */
    private void initData() {
        final String path = Environment.getExternalStorageDirectory() + "/Download";
//        bookDBHelper.insertBook("1", );
//        bookDBHelper.insertBook("2", path + "/步履不停.epub");
//        bookDBHelper.insertBook("3", path + "/艺术的故事.epub");
//        bookDBHelper.insertBook("4", path + "/我的前半生.epub");
//        bookDBHelper.insertBook("5", path + "/百年孤独.epub");
//        bookDBHelper.insertBook("6", path + "/活着.epub");
//        bookDBHelper.insertBook("7", path + "/人间失格.epub");
//        bookDBHelper.insertBook("8", path + "/月亮与六便士.epub");
//
//        bookOfShelfList.add(new BookOfShelf("1", "芳华", ));
//        bookOfShelfList.add(new BookOfShelf("2", "步履不停", ""));
//        bookOfShelfList.add(new BookOfShelf("3", ""));
//        bookOfShelfList.add(new BookOfShelf("4", "", ""));
//        bookOfShelfList.add(new BookOfShelf("5", "", ""));
//        bookOfShelfList.add(new BookOfShelf("6", "活着", "https://img3.doubanio.com/lpic/s27279654.jpg"));
//        bookOfShelfList.add(new BookOfShelf("7", "人间失格", "https://img3.doubanio.com/lpic/s6100756.jpg"));
//        bookOfShelfList.add(new BookOfShelf("8", "月亮与六便士", "https://img1.doubanio.com/lpic/s2659208.jpg"));

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
