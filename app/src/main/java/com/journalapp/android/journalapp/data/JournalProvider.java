package com.journalapp.android.journalapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.journalapp.android.journalapp.utilities.JournalAppUtils;


public class JournalProvider extends ContentProvider {


    public static final int CODE_JOURNAL = 100;
    public static final int CODE_JOURNAL_WITH_DATE = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private JournalDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {


        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = JournalContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, JournalContract.PATH_JOURNAL, CODE_JOURNAL);


        matcher.addURI(authority, JournalContract.PATH_JOURNAL + "/#", CODE_JOURNAL_WITH_DATE);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new JournalDbHelper(getContext());

        return true;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_JOURNAL:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long journalDate =
                                value.getAsLong(JournalContract.JournalEntry.COLUMN_DATE);
                        /*if (!JournalAppUtils.isDateNormalized(journalDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }*/
                        Log.d("", "insertFakeData: ----------------------------------------------------------------> ");
                        long _id = db.insert(JournalContract.JournalEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }


                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {


            case CODE_JOURNAL_WITH_DATE: {


                String normalizedUtcDateString = uri.getLastPathSegment();


                String[] selectionArguments = new String[]{normalizedUtcDateString};

                cursor = mOpenHelper.getReadableDatabase().query(

                        JournalContract.JournalEntry.TABLE_NAME,

                        projection,

                        JournalContract.JournalEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_JOURNAL: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        JournalContract.JournalEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {


        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_JOURNAL:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        JournalContract.JournalEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("");
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null; // URI to be returned

        switch (sUriMatcher.match(uri)) {

            case CODE_JOURNAL:
                db.beginTransaction();
                int rowsInserted = 0;
                try {

                        long _id = db.insert(JournalContract.JournalEntry.TABLE_NAME, null, values);
                    returnUri = ContentUris.withAppendedId(JournalContract.JournalEntry.CONTENT_URI, _id);

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

        }
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("");
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
