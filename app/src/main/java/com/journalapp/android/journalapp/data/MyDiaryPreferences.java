package com.journalapp.android.journalapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.journalapp.android.journalapp.R;

public class MyDiaryPreferences {
    public static String getPreferredUserName(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForUserName = context.getString(R.string.pref_user_key);
        String defaultLocation = context.getString(R.string.pref_user_default);
        return prefs.getString(keyForUserName, defaultLocation);
    }

    public static int getStyleText(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForTextStyle = context.getString(R.string.pref_text_style_key);
        String defaultTextStyle = context.getString(R.string.pref_text_style);
        String preferredTextStyle = prefs.getString(keyForTextStyle, defaultTextStyle);

        return Integer.parseInt(preferredTextStyle);
    }
}
