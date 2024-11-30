package com.example.ticketmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "saved_events";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DATE = "date";
    private static final String COL_PRICE = "price";
    private static final String COL_URL = "url";
    private static final String COL_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_PRICE + " TEXT, " +
                COL_URL + " TEXT, " +
                COL_IMAGE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, event.getEventName());
        values.put(COL_DATE, event.getStartDate());
        values.put(COL_PRICE, event.getPriceRange());
        values.put(COL_URL, event.getUrl());
        values.put(COL_IMAGE, event.getImageUrl());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Event> getAllSavedEvents() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
                String url = cursor.getString(cursor.getColumnIndex(COL_URL));
                String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
                events.add(new Event(name, date, price, url, image));
            }
            cursor.close();
        }
        return events;
    }

    public void deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
    }
}
