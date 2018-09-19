package com.example.android.bookinventory;
// Based on Udacity's Pets program: https://github.com/udacity/ud845-Pets

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookinventory.data.BookContract;
import com.example.android.bookinventory.data.BookContract.BookEntry;
import com.example.android.bookinventory.data.BookDbHelper;

import static android.content.ContentValues.TAG;
import static com.example.android.bookinventory.data.BookContract.BookEntry.TABLE_NAME;


/**
     * {@link BookCursorAdapter} is an adapter for a list or grid view
     * that uses a {@link Cursor} of pet data as its data source. This adapter knows
     * how to create list items for each row of pet data in the {@link Cursor}.
     */
    public class BookCursorAdapter extends CursorAdapter {
    private Context mContext;
    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);
        this.mContext = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        final TextView summary2TextView = (TextView) view.findViewById(R.id.summary2);

        // Find the columns of books attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE_BOOK);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY_BOOK);

        // Read the book attributes from the Cursor for the current book
        String bookName = cursor.getString(nameColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current book
        nameTextView.setText(bookName);
        summaryTextView.setText(bookPrice);
        summary2TextView.setText(bookQuantity);

        //Button onClick to reduce quantity
        Button button = view.findViewById(R.id.sale);
        int columnIdIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        button.setOnClickListener(new OnItemClickListener(cursor.getInt(columnIdIndex)));
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int position;

        public OnItemClickListener(int position) {
            super();
            this.position = position;
        }

        //Get the quantity from the database
        @Override
        public void onClick(View view) {

            int columnIndex = position;

            SQLiteOpenHelper helper = new BookDbHelper(mContext);
            SQLiteDatabase db = helper.getReadableDatabase();


            Cursor cursor = db.rawQuery("SELECT " + BookContract.BookEntry.COLUMN_QUANTITY_BOOK + " FROM " + TABLE_NAME + " WHERE " +
                    BookContract.BookEntry._ID + " = " + columnIndex + "", null);

            if (cursor != null && cursor.moveToFirst()) {
                String quan = cursor.getString(cursor.getColumnIndex("quantity"));
                cursor.close();

                if (mContext instanceof BookActivity) {
                    ((BookActivity) mContext).decreaseCount(columnIndex, Integer.valueOf(quan));
                }

            }

            db.close();
        }

    }
}



