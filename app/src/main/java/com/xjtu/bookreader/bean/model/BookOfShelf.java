package com.xjtu.bookreader.bean.model;




import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * 书架中的书籍表
 */

public class BookOfShelf extends DataSupport implements Serializable {

    // 书籍id
//    @Column(unique = true, nullable = true)
    private long id;

    // 书籍标题
    private String title;

    // 作者
    private String author;

    // 书籍评分
    private String rating;

    // 书籍主图片
    private String coverImage;

    // 上次打开时间
    private Date lastTime;

    // 书籍总页数
    private int pages;

    // 进度
    private int progress;

    // 是否已经下载
    private boolean isDownloaded;

    // 如果文件已经下载，则保存文件位置
    private String bookPath;

    // 文件大小
    private long fileSize;

    public BookOfShelf() {

    }

    public BookOfShelf(long id, String title) {
        this.id = id;
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
