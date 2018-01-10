package com.xjtu.bookreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by obaro on 02/04/2015.
 * 用于记录下载后书籍的位置
 */
public class BookDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "download_book.db";
    private static final int DATABASE_VERSION = 2;

    public static final String BOOK_TABLE_NAME = "book";
    public static final String BOOK_COLUMN_BOOK_ID = "book_id";
    public static final String BOOK_COLUMN_BOOK_PATH = "book_path";
//    public static final String BOOK_COLUMN_HIGHLIGHT_PATH = "highlight_path";

    public BookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + "(" + BOOK_COLUMN_BOOK_ID + " VARCHAR(50) PRIMARY KEY, " +
                        BOOK_COLUMN_BOOK_PATH + " VARCHAR(100)) "
//                        BOOK_COLUMN_HIGHLIGHT_PATH + " VARCHAR(100))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBook(String bookId, String booPath) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOK_COLUMN_BOOK_ID, bookId);
        contentValues.put(BOOK_COLUMN_BOOK_PATH, booPath);
//        contentValues.put(BOOK_COLUMN_HIGHLIGHT_PATH, highlightPath);

        db.insert(BOOK_TABLE_NAME, null, contentValues);
//        db.close();
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BOOK_TABLE_NAME);
//        db.close();
        return numRows;
    }

    public boolean updateBook(String bookId, String booPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_COLUMN_BOOK_ID, bookId);
        contentValues.put(BOOK_COLUMN_BOOK_PATH, booPath);
//        contentValues.put(BOOK_COLUMN_HIGHLIGHT_PATH, highlightPath);
        db.update(BOOK_TABLE_NAME, contentValues, BOOK_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
//        db.close();
        return true;
    }

    public Integer deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(BOOK_TABLE_NAME, BOOK_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
//        db.close();
        return count;
    }

    public Cursor getBook(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + BOOK_TABLE_NAME + " WHERE " + BOOK_COLUMN_BOOK_ID + " = " + bookId, null);
//        db.close();
        return res;
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + BOOK_TABLE_NAME, null);
//        db.close();
        return res;
    }
}