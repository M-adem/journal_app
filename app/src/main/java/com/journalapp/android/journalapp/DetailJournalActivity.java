package com.journalapp.android.journalapp;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.journalapp.android.journalapp.data.JournalContract;
import com.journalapp.android.journalapp.utilities.JournalAppDateUtils;
import com.journalapp.android.journalapp.utilities.JournalAppUtils;

public class DetailJournalActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MAIN_JOURNAL_PROJECTION = {
            JournalContract.JournalEntry._ID,
            JournalContract.JournalEntry.COLUMN_DATE,
            JournalContract.JournalEntry.COLUMN_FEELING,
            JournalContract.JournalEntry.COLUMN_NOTE


    };

    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_DATE = 1;
    public static final int INDEX_COLUMN_FEELING =2;
    public static final int INDEX_COLUMN_NOTE = 3;


    private static final int ID_DETAIL_LOADER = 353;



    private static int indexSelected;

    private Uri mUri;


    private TextView mDateView;
    private TextView mDateTimeView;
    private TextView mNoteView;
    private TextView mFeelingView;
    private ImageView mFeelingImageView;


    private static final String JOURNAL_SHARE_HASHTAG = " #DiaryApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_journal);

        mDateView = (TextView) findViewById(R.id.date_notedetail);
        mDateTimeView = (TextView) findViewById(R.id.timedetail);
        mNoteView = (TextView) findViewById(R.id.notedetail);
        mFeelingView = (TextView) findViewById(R.id.feelingdetail);
        mFeelingImageView = (ImageView) findViewById(R.id.imogy_icondetail);


        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

       /* Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mDiaryId = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mNoteView.setText(mDiaryId);
            }
        }*/
    }

    private Intent createShareJournalIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("" + JOURNAL_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareJournalIntent());
        return true;
    }


    public void deleteItem(){
        this.getContentResolver().delete(JournalContract.JournalEntry.CONTENT_URI," _ID == "+indexSelected  ,null);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }


        if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_delete_title));
            builder.setMessage(getString(R.string.dialog_delete_mesg));
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    deleteItem();
                    //Toast.makeText(getApplicationContext(), "You've choosen to delete all records", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MAIN_JOURNAL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {

            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {

            return;
        }
        indexSelected=data.getInt(INDEX_COLUMN_ID);
        long date = data.getLong(INDEX_COLUMN_DATE);
        String note = data.getString(INDEX_COLUMN_NOTE);
        int feeling = data.getInt(INDEX_COLUMN_FEELING);

        mDateView.setText(JournalAppDateUtils.getFriendlyDateString(this, date, true));
        mDateTimeView.setText(JournalAppUtils.getTime(date));
        mNoteView.setText(note);
        mFeelingView.setText(JournalAppUtils.getFeeling(this, feeling));
        mFeelingImageView.setImageResource(JournalAppUtils.getFeelingSrc(feeling));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
