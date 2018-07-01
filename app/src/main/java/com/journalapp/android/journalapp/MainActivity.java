package com.journalapp.android.journalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.journalapp.android.journalapp.data.JournalContract;
import com.journalapp.android.journalapp.utilities.FakeDataUtils;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,JournalAdapter.JournalAdapterOnClickHandler
        , SharedPreferences.OnSharedPreferenceChangeListener {

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;

    private JournalAdapter mJournalAdapter;



    private static final int DIARY_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private FloatingActionButton fab ;

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



    private static final int ID_FORECAST_LOADER = 44;


    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0f);

        FakeDataUtils.insertFakeData(this);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_Journal);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mJournalAdapter = new JournalAdapter(this, this);

        mRecyclerView.setAdapter(mJournalAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddJournalActivity();

            }
        });
        int loaderId = DIARY_LOADER_ID;
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        showLoading();

        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);
    }

    public void openAddJournalActivity(){
        Class addJournalClass = AddJournalActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, addJournalClass);
        startActivity(intentToStartDetailActivity);
    }
    @Override
    public void onClick(int journalId) {

        Intent weatherDetailIntent = new Intent(MainActivity.this, DetailJournalActivity.class);

        Uri uriForDateClicked = JournalContract.JournalEntry.buildJournalUriWithID(journalId);
        weatherDetailIntent.setData(uriForDateClicked);
        startActivity(weatherDetailIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.journal, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();


        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d("", "onStart: preferences were updated");
            mJournalAdapter.notifyDataSetChanged();
            Context context = this;
            Toast.makeText(context, "update", Toast.LENGTH_SHORT)
                    .show();
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        PREFERENCES_HAVE_BEEN_UPDATED = true;
        mJournalAdapter.notifyDataSetChanged();
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {

            case ID_FORECAST_LOADER:

                Uri forecastQueryUri = JournalContract.JournalEntry.CONTENT_URI;

                String sortOrder =  JournalContract.JournalEntry.COLUMN_DATE + " DESC";

                String selection =  JournalContract.JournalEntry.getSqlSelectJournal();

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_JOURNAL_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mJournalAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showJournalDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mJournalAdapter.swapCursor(null);
    }

    private void showJournalDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Uri forecastQueryUri = JournalContract.JournalEntry.CONTENT_URI;

        String sortOrder =  JournalContract.JournalEntry.COLUMN_DATE + " DESC";

        String selection =  JournalContract.JournalEntry.getSqlSelectJournal();

        new CursorLoader(this,
                forecastQueryUri,
                MAIN_JOURNAL_PROJECTION,
                selection,
                null,
                sortOrder);
        mJournalAdapter.swapCursor(null);
        mJournalAdapter.notifyDataSetChanged();

    }
}
