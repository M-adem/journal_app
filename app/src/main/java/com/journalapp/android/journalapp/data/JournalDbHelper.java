package com.journalapp.android.journalapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.journalapp.android.journalapp.data.JournalContract.JournalEntry;

public class JournalDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "diary.db";

    private static final int DATABASE_VERSION = 3;

    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_JOURNAL_TABLE =

                "CREATE TABLE " + JournalEntry.TABLE_NAME + " (" +
                        JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        JournalEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        JournalEntry.COLUMN_FEELING + " INTEGER NOT NULL, " +
                        JournalEntry.COLUMN_NOTE + " TEXT" + ");";


        sqLiteDatabase.execSQL(SQL_CREATE_JOURNAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
