package com.journalapp.android.journalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.journalapp.android.journalapp.data.JournalContract;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddJournalActivity extends AppCompatActivity  {
    Calendar currentDate;
    int mDay, mMonth, mShowMonth, mYear, mHoure, mMinute;
    String amOrPm = "";
    private Spinner spinner2;
    private TextView mdatePicker;
    private TextView mtimePicker;
    private EditText mNote;
    private FloatingActionButton fab;
    private Date dateTime=new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        mdatePicker = (TextView) findViewById(R.id.date_picker);
        mtimePicker = (TextView) findViewById(R.id.time_picker);
        currentDate = Calendar.getInstance();

        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentDate.get(Calendar.MONTH);
        mYear = currentDate.get(Calendar.YEAR);

        mHoure = currentDate.get(Calendar.HOUR);
        mMinute = currentDate.get(Calendar.MINUTE);
        mShowMonth = mMonth + 1;

        mdatePicker.setText(mDay + "/" + mShowMonth + "/" + mYear);
        if (currentDate.get(Calendar.AM_PM) == Calendar.PM) {
            amOrPm = "PM";
        } else {
            amOrPm = "AM";
        }
        mtimePicker.setText(mHoure + ":" + mMinute + " " + amOrPm);

        mdatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddJournalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        mdatePicker.setText(i2 + "/" + i1 + "/" + i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        dateTime=calendar.getTime();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        mtimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddJournalActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        mtimePicker.setText(hourOfDay + ":" + minute);
                    }

                }, mHoure, mMinute, true);
                timePickerDialog.show();
            }
        });
        mNote = (EditText) findViewById(R.id.note_input);
        fab = (FloatingActionButton) findViewById(R.id.fab_valid);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openAddJournalActivity();

                insertItem();
            }
        });

        addItemsOnSpinner2();

    }
    public void insertItem(){

        ContentValues testJournalValues = new ContentValues();
        testJournalValues.put(JournalContract.JournalEntry.COLUMN_DATE, dateTime.getTime());
        testJournalValues.put(JournalContract.JournalEntry.COLUMN_FEELING, spinner2.getSelectedItemPosition()+1);
        Log.d("", ""+spinner2.getSelectedItemPosition());
        testJournalValues.put(JournalContract.JournalEntry.COLUMN_NOTE,mNote.getText().toString());

        this.getContentResolver().insert(JournalContract.JournalEntry.CONTENT_URI,testJournalValues);

        finish();

    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.felling_angry));

        list.add(getString(R.string.felling_exacited));

        list.add(getString(R.string.felling_loved));

        list.add(getString(R.string.felling_none));

        list.add(getString(R.string.felling_sade));

        list.add(getString(R.string.felling_sick));
        list.add(getString(R.string.felling_silly));

        list.add(getString(R.string.felling_stressed));
        list.add(getString(R.string.felling_happy));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }




}
