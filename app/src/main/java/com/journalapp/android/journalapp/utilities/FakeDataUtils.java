package com.journalapp.android.journalapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.journalapp.android.journalapp.data.JournalContract.JournalEntry;
import com.journalapp.android.journalapp.data.JournalContract;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class FakeDataUtils {

    private static int [] JournalIDs = {100,500,200,7,9,920};

    private static ContentValues createTestJournalContentValues(long date) {
        ContentValues testJournalValues = new ContentValues();
        testJournalValues.put(JournalEntry.COLUMN_DATE, date);
        testJournalValues.put(JournalEntry.COLUMN_FEELING, getRandomNumberInRange(8));
        testJournalValues.put(JournalEntry.COLUMN_NOTE,getRandomNumberInRange(1000));
        return testJournalValues;
    }

    public static void insertFakeData(Context context) {

        long today = JournalAppUtils.normalizeDate(System.currentTimeMillis());
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();

        for(int i=0; i<2; i++) {
            fakeValues.add(FakeDataUtils.createTestJournalContentValues(today - TimeUnit.DAYS.toMillis(i)));
        }

        context.getContentResolver().bulkInsert(
                JournalContract.JournalEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[1]));
    }

    private static int getRandomNumberInRange(int max) {

        return (int) (Math.random()*max)+1;
    }
}
