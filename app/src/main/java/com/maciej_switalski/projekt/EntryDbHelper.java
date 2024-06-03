package com.maciej_switalski.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EntryDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "entry.db";
    private static final int DATABASE_VERSION = 2;

    public EntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES_TABLE = "CREATE TABLE " +
                EntryContract.EntryConst.TABLE_NAME + " (" +
                EntryContract.EntryConst._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EntryContract.EntryConst.COLUMN_TITLE + " TEXT NOT NULL, " +
                EntryContract.EntryConst.COLUMN_CONTENT + " TEXT NOT NULL, " +
                EntryContract.EntryConst.COLUMN_CATEGORY + " TEXT, " +
                EntryContract.EntryConst.COLUMN_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EntryContract.EntryConst.TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new entry
    public long addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EntryContract.EntryConst.COLUMN_TITLE, entry.getTitle());
        values.put(EntryContract.EntryConst.COLUMN_CONTENT, entry.getContent());
        values.put(EntryContract.EntryConst.COLUMN_DATE, entry.getDate());
        values.put(EntryContract.EntryConst.COLUMN_CATEGORY, entry.getCategory());

        return db.insert(EntryContract.EntryConst.TABLE_NAME, null, values);

    }

    public void updateEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EntryContract.EntryConst.COLUMN_TITLE, entry.getTitle());
        values.put(EntryContract.EntryConst.COLUMN_CONTENT, entry.getContent());
        values.put(EntryContract.EntryConst.COLUMN_DATE, entry.getDate());
        values.put(EntryContract.EntryConst.COLUMN_CATEGORY, entry.getCategory());

        String selection = EntryContract.EntryConst._ID + " = ?";
        String[] selectionArgs = {String.valueOf(entry.getId())};
        db.update(EntryContract.EntryConst.TABLE_NAME, values, selection, selectionArgs);

    }

    // Method to retrieve all entries
    public List<Entry> getAllEntries() {
        List<Entry> entryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                EntryContract.EntryConst.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(EntryContract.EntryConst._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(EntryContract.EntryConst.COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(EntryContract.EntryConst.COLUMN_CONTENT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(EntryContract.EntryConst.COLUMN_DATE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(EntryContract.EntryConst.COLUMN_CATEGORY));


                entryList.add(new Entry(id, title, content, date, category));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return entryList;
    }


    public void removeEntry(long entryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = EntryContract.EntryConst._ID + " = ?";
        String[] selectionArgs = {String.valueOf(entryId)};
        db.delete(EntryContract.EntryConst.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Execute SQL statement to delete all entries
        db.execSQL("DELETE FROM " + EntryContract.EntryConst.TABLE_NAME);
    }
}
