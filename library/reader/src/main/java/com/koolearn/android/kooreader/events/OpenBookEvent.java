package com.koolearn.android.kooreader.events;
public class OpenBookEvent {

    public final String bookPath;
    public OpenBookEvent(String bookPath) {
        this.bookPath = bookPath;
    }
}