package com.xjtu.bookreader.bean;

import android.databinding.Bindable;

import com.xjtu.bookreader.bean.book.BooksBean;
import com.xjtu.bookreader.http.ParamNames;

import java.io.Serializable;

/**
 * Created by jingbin on 2016/11/29.
 * 轮播图bean
 */

public class BannerItemBean implements Serializable {


    @ParamNames("id") // 书籍id
    private long id;

    @ParamNames("title")  // 书籍标题
    private String title;

    @ParamNames("image")  // 推荐图片
    private String image;

    public BannerItemBean() {
    }


    public BannerItemBean(long id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    //    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
