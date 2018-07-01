package com.journalapp.android.journalapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.journalapp.android.journalapp.data.JournalContract;
import com.journalapp.android.journalapp.data.MyDiaryPreferences;
import com.journalapp.android.journalapp.utilities.JournalAppDateUtils;
import com.journalapp.android.journalapp.utilities.JournalAppUtils;

import java.util.List;


public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalAdapterViewHolder> {



    private final JournalAdapterOnClickHandler mClickHandler;
    private final Context mContext;
    private Cursor mCursor;

    public interface JournalAdapterOnClickHandler {
        void onClick(int journalId);
    }

    public JournalAdapter(@NonNull Context mContext, JournalAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.mContext = mContext;
    }



    public class JournalAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView mNoteTextView;
        public TextView mDateTextView;
        public TextView mTimeTextView;
        public TextView mFeelingTextView;
        public ImageView mImogyIconImageView;

        public JournalAdapterViewHolder(View view) {
            super(view);
            mNoteTextView = (TextView) view.findViewById(R.id.note);
            mDateTextView = (TextView) view.findViewById(R.id.date_note);
            mTimeTextView = (TextView) view.findViewById(R.id.time);
            mFeelingTextView = (TextView) view.findViewById(R.id.feeling);
            mImogyIconImageView = (ImageView) view.findViewById(R.id.imogy_icon);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int journalColumnIndex = mCursor.getInt(MainActivity.INDEX_COLUMN_ID);
            Log.d("", "onClick: ------------->  "+journalColumnIndex);

            mClickHandler.onClick(journalColumnIndex);
        }
    }


    @Override
    public JournalAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.journal_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new JournalAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JournalAdapterViewHolder journalAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_COLUMN_DATE);
        String note = mCursor.getString(MainActivity.INDEX_COLUMN_NOTE);
        int feeling = mCursor.getInt(MainActivity.INDEX_COLUMN_FEELING);
        Context context = journalAdapterViewHolder.itemView.getContext();
        journalAdapterViewHolder.mNoteTextView.setText(note);
        journalAdapterViewHolder.mDateTextView.setText(JournalAppDateUtils.getFriendlyDateString(context, dateInMillis, true));
        journalAdapterViewHolder.mTimeTextView.setText(JournalAppUtils.getTime(dateInMillis));
        journalAdapterViewHolder.mFeelingTextView.setText(JournalAppUtils.getFeeling(context, feeling));
        journalAdapterViewHolder.mImogyIconImageView.setImageResource(JournalAppUtils.getFeelingSrc(feeling));


        if (MyDiaryPreferences.getStyleText(context) == 0) {
            journalAdapterViewHolder.mNoteTextView.setTextSize(20);
            journalAdapterViewHolder.mNoteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        if (MyDiaryPreferences.getStyleText(context) == 1) {
            journalAdapterViewHolder.mNoteTextView.setTextSize(20);
            journalAdapterViewHolder.mNoteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        if (MyDiaryPreferences.getStyleText(context) == 2) {
            journalAdapterViewHolder.mNoteTextView.setTextSize(20);
            journalAdapterViewHolder.mNoteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }

        if (MyDiaryPreferences.getStyleText(context) == 3) {
            journalAdapterViewHolder.mNoteTextView.setTextSize(20);
            journalAdapterViewHolder.mNoteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        }


    }

    @Override
    public int getItemCount() {

        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}