package com.xjtu.bookreader.model;


import com.xjtu.bookreader.R;
import com.xjtu.bookreader.bean.BannerBean;
import com.xjtu.bookreader.bean.BannerItemBean;
import com.xjtu.bookreader.bean.MallRecommendBean;
import com.xjtu.bookreader.bean.MallRecommendItemBean;
import com.xjtu.bookreader.http.HttpClient;
import com.xjtu.bookreader.http.RequestImpl;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.xjtu.bookreader.util.StringResourceUtil.getStringById;

/**
 * Created by jingbin on 2016/12/1.
 * 每日推荐model
 * 需要改为书城model
 */

public class MallModel {

    private String year = "2018";
    private String month = "1";
    private String day = "8";


    // 请求书籍数据
    private String mType = "综合";
    // 开始请求的角标
    private int mStart = 0;
    // 一次请求的数量
    private int mCount = 18;

    public void setDate(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * 轮播图
     * 也是展示推荐的书籍，需要返回图片和书籍id
     */
    public void showBannerPage(final RequestImpl listener) {
        BannerBean bannerBean = new BannerBean();
        List<BannerItemBean> bannerItemBeans = new ArrayList<>();
        bannerItemBeans.add(new BannerItemBean(10001, "芳华", "http://img3.jiemian.com/101/original/20170522/149545357274009400.jpg"));
        bannerItemBeans.add(new BannerItemBean(10002, "步履不停", "http://img.suilengea.com/?tag=a&url=mmbizz-zqpicz-zcn/mmbiz_jpg/XKl38Yp6CNia641f0cewp42gS4vaBI2FWa5GmibOJC39NXgSib7Hz1W5X2V2zVT7448wjAAEl1eYwQibrwSF7FjdMw/0?wx_fmt=jpeg"));
        bannerItemBeans.add(new BannerItemBean(10008, "月亮与六便士", "https://upload-images.jianshu.io/upload_images/2243916-b47c3c92f9c3032f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/476"));
        bannerBean.setResults(bannerItemBeans);
        bannerBean.setError(false);
        listener.loadSuccess(bannerBean);

//        Subscription subscription = HttpClient.Builder.getRecommendService().getBanner(year, month, day)
//                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .subscribe(new Observer<BannerBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        listener.loadFailed();
//                    }
//
//                    @Override
//                    public void onNext(BannerBean bannerBean) {
//                        listener.loadSuccess(bannerBean);
//                    }
//                });
//        listener.addSubscription(subscription);
    }


    /**
     * 显示RecyclerView数据
     * 加载推荐内容
     */
    public void showRecyclerViewData(final RequestImpl listener) {
        ArrayList<List<MallRecommendItemBean>> listList = new ArrayList<List<MallRecommendItemBean>>();

        List<MallRecommendItemBean> mallRecommendItemBeans1 = new ArrayList<>();
        mallRecommendItemBeans1.add(new MallRecommendItemBean(10001, "芳华", "https://img3.doubanio.com/lpic/s29418322.jpg"));
        mallRecommendItemBeans1.add(new MallRecommendItemBean(10002, "步履不停", "http://mebook.cc/wp-content/uploads/2017/06/blob-39.png"));
        mallRecommendItemBeans1.add(new MallRecommendItemBean(10003, "艺术的故事", "https://img3.doubanio.com/lpic/s3219163.jpg"));

        List<MallRecommendItemBean> mallRecommendItemBeans2 = new ArrayList<>();
        mallRecommendItemBeans2.add(new MallRecommendItemBean(10004, "我的前半生", "https://img1.doubanio.com/lpic/s2720819.jpg"));
        mallRecommendItemBeans2.add(new MallRecommendItemBean(10005, "百年孤独", "https://img3.doubanio.com/lpic/s6384944.jpg"));
        mallRecommendItemBeans2.add(new MallRecommendItemBean(10006, "活着", "https://img3.doubanio.com/lpic/s27279654.jpg"));


        List<MallRecommendItemBean> mallRecommendItemBeans3 = new ArrayList<>();
        mallRecommendItemBeans3.add(new MallRecommendItemBean(10007, "人间失格", "https://img3.doubanio.com/lpic/s6100756.jpg"));
        mallRecommendItemBeans3.add(new MallRecommendItemBean(10008, "月亮与六便士", "https://img1.doubanio.com/lpic/s2659208.jpg"));
        mallRecommendItemBeans3.add(new MallRecommendItemBean(10009, "步履不停", "http://mebook.cc/wp-content/uploads/2017/06/blob-39.png"));



        addUrlList(listList, mallRecommendItemBeans1, getStringById(R.string.culture_book));
        addUrlList(listList, mallRecommendItemBeans2, getStringById(R.string.politics_book));
        addUrlList(listList, mallRecommendItemBeans3, getStringById(R.string.history_book));
        addUrlList(listList, mallRecommendItemBeans3, getStringById(R.string.economics_book));

        listener.loadSuccess(listList);


//        Func1<MallRecommendBean, Observable<List<List<MallRecommendItemBean>>>> func1 = new Func1<MallRecommendBean, Observable<List<List<MallRecommendItemBean>>>>() {
//            @Override
//            public Observable<List<List<MallRecommendItemBean>>> call(MallRecommendBean mallBean) {
//
//                List<List<MallRecommendItemBean>> lists = new ArrayList<>();
//                MallRecommendBean.ResultsBean results = mallBean.getResults();
//
//
//                if (results.getCultureBooks() != null && results.getCultureBooks().size() > 0) {
//                    addUrlList(lists, results.getCultureBooks(), getStringById(R.string.culture_book));
//                }
//
//                if (results.getPoliticsBooks() != null && results.getPoliticsBooks().size() > 0) {
//                    addUrlList(lists, results.getPoliticsBooks(), getStringById(R.string.politics_book));
//                }
//                if (results.getHistoryBooks() != null && results.getHistoryBooks().size() > 0) {
//                    addUrlList(lists, results.getHistoryBooks(), getStringById(R.string.history_book));
//                }
//                if (results.getEconomicsBooks() != null && results.getEconomicsBooks().size() > 0) {
//                    addUrlList(lists, results.getEconomicsBooks(), getStringById(R.string.economics_book));
//                }
//
//                return Observable.just(lists);
//            }
//        };

//        Observer<List<List<MallRecommendItemBean>>> observer = new Observer<List<List<MallRecommendItemBean>>>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                listener.loadFailed();
//            }
//
//            @Override
//            public void onNext(List<List<MallRecommendItemBean>> lists) {
//                listener.loadSuccess(lists);
//            }
//        };
//
//        Subscription subscription = HttpClient.Builder.getRecommendService().getRecommend(year, month, day)
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .flatMap(func1)
//                .subscribe(observer);
//
//        listener.addSubscription(subscription);
    }


    // subList没有实现序列化！缓存时会出错！
    private void addUrlList(List<List<MallRecommendItemBean>> lists, List<MallRecommendItemBean> arrayList, String typeTitle) {

        // title
        MallRecommendItemBean bean = new MallRecommendItemBean();
        bean.setType_title(typeTitle);

        ArrayList<MallRecommendItemBean> beans = new ArrayList<>();
        beans.add(bean);

        lists.add(beans);

        int arrayListSize = arrayList.size();

        // 先只返回3记录
        if (arrayListSize > 0 && arrayListSize < 4) {

            lists.add(arrayList);
        }else {
            lists.add(arrayList.subList(0, 4));
        }
    }






}
