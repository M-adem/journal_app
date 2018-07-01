package com.journalapp.android.journalapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Diary {

public String _id;
    public String note;
    public long date_note;
    public int feeling;

    public Diary(String note, long date_note, int feeling) {
        this.note = note;
        this.date_note = date_note;
        this.feeling = feeling;
    }

    public Diary(String _id, String note, long date_note, int feeling) {
        this._id = _id;
        this.note = note;
        this.date_note = date_note;
        this.feeling = feeling;
    }

    public Diary() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public long getDate_note() {
        return date_note;
    }

    public void setDate_note(long date_note) {
        this.date_note = date_note;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getTime() {
        String strDateFormat = "HH:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        return sdf.format(getDate_note());
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id =  id;
    }
}
