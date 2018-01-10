package com.koolearn.android.kooreader.events;

public class AddBookEvent {
    public final String bookPath;
    public AddBookEvent(String bookPath) {
        this.bookPath = bookPath;
    }
}