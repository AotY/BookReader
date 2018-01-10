package com.xjtu.bookreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by obaro on 02/04/2015.
 * 书架
 */
public class ShelfDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shelf.db";
    private static final int DATABASE_VERSION = 1;

    public static final String SHELF_TABLE_NAME = "book";
    public static final String SHELF_COLUMN_ID = "_id";
    public static final String SHELF_COLUMN_BOOK_ID = "book_id";

    public ShelfDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + SHELF_TABLE_NAME +
                        "(" + SHELF_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        SHELF_COLUMN_BOOK_ID + " VARCHAR(50))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SHELF_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHELF_COLUMN_BOOK_ID, bookId);

        db.insert(SHELF_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SHELF_TABLE_NAME);
        return numRows;
    }

    public boolean updateBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHELF_COLUMN_BOOK_ID, bookId);
        db.update(SHELF_TABLE_NAME, contentValues, SHELF_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
        return true;
    }

    public Integer deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SHELF_TABLE_NAME, SHELF_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
    }

    public Cursor getBook(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SHELF_TABLE_NAME + " WHERE " + SHELF_COLUMN_BOOK_ID + " = ? ", new String[]{bookId});
        return res;
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SHELF_TABLE_NAME, null);
        return res;
    }
}