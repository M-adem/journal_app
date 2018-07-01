package com.journalapp.android.journalapp.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.journalapp.android.journalapp.utilities.JournalAppUtils;

public class JournalContract {
    public static final String CONTENT_AUTHORITY = "com.journalapp.android.journalapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_JOURNAL = "journal";

    public static final class JournalEntry implements BaseColumns {





        public static final String TABLE_NAME = "journal";

        public static final String _ID = "_ID";
        public static final String COLUMN_DATE = "date";


        public static final String COLUMN_NOTE = "note";

        public static final String COLUMN_FEELING = "feeling";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_JOURNAL)
                .build();



        public static Uri buildJournalUriWithID(int idJournal) {

            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(idJournal))
                    .build();
        }
        public static String getSqlSelectJournal() {
            return JournalContract.JournalEntry.COLUMN_DATE ;
        }
    }
}
