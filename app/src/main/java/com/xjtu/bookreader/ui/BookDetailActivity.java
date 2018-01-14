package com.xjtu.bookreader.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.kooreader.book.Book;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.base.BaseHeaderActivity;
import com.xjtu.bookreader.bean.book.BookDetailBean;
import com.xjtu.bookreader.databinding.ActivityBookDetailBinding;
import com.xjtu.bookreader.databinding.DetailHeaderBookDetailBinding;
import com.xjtu.bookreader.util.CommonUtils;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.KooreaderUtil;
import com.xjtu.bookreader.util.PerfectClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 没有加入书架前可以查看书架详细信息
 * 其实只要传入书籍id就可以了的。
 */
public class BookDetailActivity extends BaseHeaderActivity<DetailHeaderBookDetailBinding, ActivityBookDetailBinding> {

    // 数据id
    private String bookId;

    private BookDetailBean bookDetailBean;

    private String mBookDetailUrl;

    private String mBookDetailName;

    public final static String EXTRA_PARAM = "book_id";

    private final BookCollectionShadow myCollection = new BookCollectionShadow();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        KooreaderUtil.verifyStoragePermissions(this);

        if (getIntent() != null) {
            bookId = getIntent().getStringExtra(EXTRA_PARAM);
        }

        // 这里应该就是设置图片移动效果
        setMotion(setHeaderPicView(), true);

        //
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());

        loadBookDetail();

        initClick();
    }

    private void initClick() {
        bindingHeaderView.btnReading.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                myCollection.bindToService(BookDetailActivity.this, new Runnable() {
                    public void run() {
                        final String bookPath = Environment.getExternalStorageDirectory() + "/Download" + "/活着.epub";
                        Book book = myCollection.getBookByFile(bookPath);
                        if (book != null) {
                            KooreaderUtil.openBook(BookDetailActivity.this, book);
                        } else {
                            Toast.makeText(BookDetailActivity.this, "打开失败,请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 跳转到阅读Activity
     *
     * @param data
     */
    private void openBook(Book data) {
        KooReader.openBookActivity(this, data, null);
        this.overridePendingTransition(com.ninestars.android.R.anim.tran_fade_in, com.ninestars.android.R.anim.tran_fade_out);
    }

    @Override
    protected int setHeaderLayout() {
        return R.layout.detail_header_book_detail;
    }

    private void loadBookDetail() {
        DebugUtil.error("------http2");
        BookDetailBean bookDetailBean = new BookDetailBean();
        bookDetailBean.setId("6");
        bookDetailBean.setTitle("活着");
        bookDetailBean.setImage("https://img3.doubanio.com/lpic/s27279654.jpg");
        bookDetailBean.setPublisher("作家出版社");
        bookDetailBean.setPubdate("2012-8");
        bookDetailBean.setPages("191");
        bookDetailBean.setPrice("20.00元");
        bookDetailBean.setRating("9.3");
        bookDetailBean.setAuthor_intro("余华，1960年出生，1983年开始写作。至今已经出版长篇小说4部，中短篇小说集6部，随笔集4部。主要作品有《兄弟》《活着》《许三观卖血记》《在细雨中呼喊》等。\n 其作品已被翻译成20多种语言在美国、英国、法国、德国、意大利、西班牙、荷兰、瑞典、挪威、希腊、俄罗斯、保加利亚、匈牙利、捷克、塞尔维亚、斯洛伐克、波兰、巴西、以色列、日本、韩国、越南、泰国和印度等国出版。曾获意大利格林扎纳·卡佛文学奖（1998年）、法国文学和艺术骑士勋章（2004年）、中华图书特殊贡献奖（2005年）、法国国际信使外国小说奖（2008年）等。");

        List<String> authors = new ArrayList<>();
        authors.add("余华");
        bookDetailBean.setAuthor(authors);
        bookDetailBean.setSummary("《活着(新版)》讲述了农村人福贵悲惨的人生遭遇。福贵本是个阔少爷，可他嗜赌如命，终于赌光了家业，一贫如洗。他的父亲被他活活气死，母亲则在穷困中患了重病，福贵前去求药，却在途中被国民党抓去当壮丁。经过几番波折回到家里，才知道母亲早已去世，妻子家珍含辛茹苦地养大两个儿女。此后更加悲惨的命运一次又一次降临到福贵身上，他的妻子、儿女和孙子相继死去，最后只剩福贵和一头老牛相依为命，但老人依旧活着，仿佛比往日更加洒脱与坚强。\n" +
                "\n" +
                "《活着(新版)》荣获意大利格林扎纳•卡佛文学奖最高奖项（1998年）、台湾《中国时报》10本好书奖（1994年）、香港“博益”15本好书奖（1994年）、第三届世界华文“冰心文学奖”（2002年），入选香港《亚洲周刊》评选的“20世纪中文小说百年百强”、中国百位批评家和文学编辑评选的“20世纪90年代最有影响的10部作品");

        bookDetailBean.setUrl("https://book.douban.com/subject/4913064/");
        bookDetailBean.setAlt("https://book.douban.com/subject/4913064/");
        bookDetailBean.setIsbn13("9787506365437");
        bookDetailBean.setCatalog("中文版自序\n" +
                "韩文版自序\n" +
                "日文版自序\n" +
                "英文版自序\n" +
                "麦田新版自序\n" +
                "活着\n" +
                "外文版评论摘要");

        mBookDetailUrl = bookDetailBean.getAlt();
        mBookDetailName = bookDetailBean.getTitle();

        setTitle(bookDetailBean.getTitle());
        setSubTitle("作者：" + bookDetailBean.getAuthor());

        bindingHeaderView.setBookDetailBean(bookDetailBean);
        bindingHeaderView.executePendingBindings();

        bindingContentView.setBookDetailBean(bookDetailBean);
        bindingContentView.executePendingBindings();
        showContentView();

//        Subscription get = HttpClient.Builder.getDouBanService().getBookDetail(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BookDetailBean>() {
//                    @Override
//                    public void onCompleted() {
//                        showContentView();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showError();
//                    }
//
//                    @Override
//                    public void onNext(final BookDetailBean bookDetailBean) {
//
//                        mBookDetailUrl = bookDetailBean.getAlt();
//                        mBookDetailName = bookDetailBean.getTitle();
//
//                        setTitle(bookDetailBean.getTitle());
//                        setSubTitle("作者：" + bookDetailBean.getAuthor());
//
//                        bindingHeaderView.setBookDetailBean(bookDetailBean);
//                        bindingHeaderView.executePendingBindings();
//
//                        bindingContentView.setBookDetailBean(bookDetailBean);
//                        bindingContentView.executePendingBindings();
//                    }
//                });
//        addSubscription(get);
    }

    @Override
    protected void setTitleClickMore() {
//        WebViewActivity.loadUrl(this, mBookDetailUrl, mBookDetailName);
    }

    @Override
    protected String setHeaderImgUrl() {
        if (bookDetailBean == null) {
            return "";
        }
//        return bookDetailBean.getImages().getMedium();
        return bookDetailBean.getImage();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return bindingHeaderView.imgItemBg;
    }

    @Override
    protected ImageView setHeaderPicView() {
        return bindingHeaderView.ivOnePhoto;
    }

    @Override
    protected void onRefresh() {
        loadBookDetail();
    }

    /**
     * @param context activity
     */
    public static void start(Activity context, String id, ImageView imageView) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_PARAM, id);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, CommonUtils.getString(R.string.transition_book_img));//与xml文件对应

        ActivityCompat.startActivity(context, intent, options.toBundle());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myCollection.unbind();
    }

}
