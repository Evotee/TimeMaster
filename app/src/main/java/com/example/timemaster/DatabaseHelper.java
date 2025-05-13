package com.example.timemaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TASK = "task";

    public DatabaseHelper(Context context, String databasePath) {
        super(context, databasePath, null, DATABASE_VERSION);
        createDatabaseFolder(context);
    }

    public void clearTasksForDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_DATE + "=?", new String[]{date});
        db.close();
    }

    private void createDatabaseFolder(Context context) {
        File dbFolder = new File(context.getFilesDir(), "bd");
        if (!dbFolder.exists()) {
            dbFolder.mkdir();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TASK + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTask(String date, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TASK, task);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void copyTasksToNewDatabase(String newDatabasePath) {
        SQLiteDatabase oldDb = this.getReadableDatabase();
        SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(newDatabasePath, null);

        // Создаем таблицу в новой базе данных
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TASK + " TEXT)";
        newDb.execSQL(createTable);

        // Копируем данные
        Cursor cursor = oldDb.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put(COLUMN_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                values.put(COLUMN_TASK, cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                newDb.insert(TABLE_NAME, null, values);
            } while (cursor.moveToNext());
        }
        cursor.close();
        oldDb.close();
        newDb.close();
    }

    public List<String> getTasksForDate(String date) {
        List<String> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TASK},
                COLUMN_DATE + "=?", new String[]{date}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
