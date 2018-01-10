package com.xjtu.bookreader.bean;


import android.databinding.Bindable;

import com.xjtu.bookreader.bean.book.BooksBean;
import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;

/**
 * Created by jingbin on 2016/11/30.
 * 首页item bean
 */

public class ShelfBookItemBean implements Serializable {

    @ParamNames("id") // 书籍id
    private String id;

    @ParamNames("title")  // 书籍标题
    private String title;

    @ParamNames("author")  //
    private String author;

    @ParamNames("rating")  // 书籍评分
    private BooksBean.RatingBean rating;

    @ParamNames("image")  // 书籍主图片
    private String image;

    @ParamNames("date")  // 上次打开时间
    private String date;

    @ParamNames("finish")  // 计算完成度
    private String finish;

    public ShelfBookItemBean() {

    }

    public ShelfBookItemBean(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;

    }

    public ShelfBookItemBean(String id, String title, String author, BooksBean.RatingBean rating, String image, String date, String finish) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.image = image;
        this.date = date;
        this.finish = finish;
    }

//    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    @Bindable
    public BooksBean.RatingBean getRating() {
        return rating;
    }

    public void setRating(BooksBean.RatingBean rating) {
        this.rating = rating;
    }

//    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    @Bindable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    @Bindable
    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
