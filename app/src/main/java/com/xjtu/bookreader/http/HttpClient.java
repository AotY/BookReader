package com.xjtu.bookreader.http;


import com.xjtu.bookreader.bean.BannerBean;
import com.xjtu.bookreader.bean.CategoryRankingBean;
import com.xjtu.bookreader.bean.MallRecommendBean;
import com.xjtu.bookreader.bean.book.BookBean;
import com.xjtu.bookreader.bean.book.BookDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jingbin on 16/11/21.
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {
        public static HttpClient getDouBanService() {
            return HttpUtils.getInstance().getDouBanServer(HttpClient.class);
        }
        // 书城推荐内容
        public static HttpClient getRecommendService() {
            return HttpUtils.getInstance().getRecommendServer(HttpClient.class);
        }

        // 获取我的书架列表
//        public static HttpClient getShelfService() {
//            return HttpUtils.getInstance().getShelfService(HttpClient.class);
//        }

        // 获取书籍列表
        public static HttpClient getBookListService() {
            return HttpUtils.getInstance().getBookListService(HttpClient.class);
        }


    }


    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     */

    @GET("v1/book/search")
    Observable<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("v1/book/{id}")
    Observable<BookDetailBean> getBookDetail(@Path("id") String id);


    @GET("v1/recommend/book")
    Observable<MallRecommendBean> getRecommend(@Query("year") String year, @Query("month") String month, @Query("day") String day);

    /**
     * 首页轮播图
     */
    @GET("v1/recommend/banner")
    Observable<BannerBean> getBanner(@Query("year") String year, @Query("month") String month, @Query("day") String day);


    /**
     * 书籍列表
     */
    @GET("v1/book_list")
    Observable<CategoryRankingBean> getBookList(@Query("type") String type, @Query("start") int start, @Query("count") int count);


}