package com.xjtu.bookreader.bean;


import android.databinding.Bindable;

import com.xjtu.bookreader.bean.book.BooksBean;
import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/30.
 * 首页item bean
 */

public class MallRecommendItemBean implements Serializable {

    // 存储单独设置的值，用来显示title
    @ParamNames("type_title")
    private String type_title;

    @ParamNames("id") // 书籍id
    private String id;

    @ParamNames("title")  // 书籍标题
    private String title;

    @ParamNames("rating")  // 书籍评分
    private String rating;

    @ParamNames("image")  // 书籍主图片
    private String image;

    public MallRecommendItemBean() {

    }

    public MallRecommendItemBean(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public MallRecommendItemBean(String type_title, String id, String title, String rating, String image) {
        this.type_title = type_title;
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.image = image;
    }

    public String getType_title() {
        return type_title;
    }

    public void setType_title(String type_title) {
        this.type_title = type_title;
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
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

//    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
