package com.journalapp.android.journalapp.utilities;

import android.content.Context;

import com.journalapp.android.journalapp.R;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public final class JournalAppUtils {
    public static final int ANGRY = 1;
    public static final int EXCITED = 2;
    public static final int LOVED = 3;
    public static final int NONE_FEELING = 4;
    public static final int SADE = 5;
    public static final int SICK = 6;
    public static final int SILLY = 7;
    public static final int STRESSED = 8;
    public static final int HAPPY = 9;

    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);
    public static String getFeeling(Context c, int num) {


        switch (num) {
            case 1:
                return c.getString(R.string.felling_angry);

            case 2:
                return c.getString(R.string.felling_exacited);

            case 3:
                return c.getString(R.string.felling_loved);

            case 4:
                return c.getString(R.string.felling_none);

            case 5:
                return c.getString(R.string.felling_sade);

            case 6:
                return c.getString(R.string.felling_sick);

            case 7:
                return c.getString(R.string.felling_silly);

            case 8:
                return c.getString(R.string.felling_stressed);
            case 9:
                return c.getString(R.string.felling_happy);


        }
        return null;
    }



    public static int getFeelingSrc(int feeling) {
        switch (feeling) {
            case 1:
                return R.drawable.ic_angry;

            case 2:
                return R.drawable.ic_excited;

            case 3:
                return R.drawable.ic_loved;

            case 4:
                return R.drawable.ic_none;

            case 5:
                return R.drawable.ic_sad;

            case 6:
                return R.drawable.ic_sick;

            case 7:
                return R.drawable.ic_silly;

            case 8:
                return R.drawable.ic_stressed;
            case 9:
                return R.drawable.ic_happy;
        }
        return 0;
    }


    public static String getTime(long date) {
        String strDateFormat = "HH:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        return sdf.format(date);
    }
    public static boolean isDateNormalized(long millisSinceEpoch) {
        boolean isDateNormalized = false;
        if (millisSinceEpoch % DAY_IN_MILLIS == 0) {
            isDateNormalized = true;
        }

        return isDateNormalized;
    }
    public static long normalizeDate(long date) {
        long daysSinceEpoch = elapsedDaysSinceEpoch(date);
        long millisFromEpochToTodayAtMidnightUtc = daysSinceEpoch * DAY_IN_MILLIS;
        return millisFromEpochToTodayAtMidnightUtc;
    }

    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }


}
