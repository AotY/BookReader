package com.xjtu.bookreader.bean;

import java.io.Serializable;

/**
 * Created by LeonTao on 2018/1/7.
 */

public class ReaderObject implements Serializable {

    // book id
    private String id;

    // 标题
    private String title;

    // 文件位置
    private String bookPath;

//    // 高亮文件位置
//    private String highlightPath;

    public ReaderObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

//    public String getHighlightPath() {
//        return highlightPath;
//    }
//
//    public void setHighlightPath(String highlightPath) {
//        this.highlightPath = highlightPath;
//    }
}

