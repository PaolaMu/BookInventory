package com.example.android.bookinventory;
// Based on Udacity's Pets program: https://github.com/udacity/ud845-Pets
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract;
import com.example.android.bookinventory.data.BookContract.BookEntry;


public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the book data
        ListView bookListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        //Setup item click listener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(BookActivity.this, EditorActivity.class);

                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                intent.setData(currentBookUri);

                startActivity(intent);

            }
        });


        // Kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    private void insertBook() {

        // Create a ContentValues object where column names are the keys,
        // and Pride and Prejudice's book attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, getString(R.string.dummy_name));
        values.put(BookEntry.COLUMN_PRICE_BOOK, getString(R.string.dummy_price));
        values.put(BookEntry.COLUMN_QUANTITY_BOOK, getString(R.string.dummy_quantity));
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, getString(R.string.dummy_supplier));
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, getString(R.string.dummy_phone));

        getContentResolver().insert(BookEntry.CONTENT_URI, values);

    }

    /**
     * Helper method to delete all books in the database.
     */
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_book.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_PRICE_BOOK,
                BookEntry.COLUMN_QUANTITY_BOOK,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    //decrease quantity button
    public void decreaseCount(int columnId, int quantity) {

        if (quantity < 1) {
            Toast.makeText(this, getString(R.string.quantity_change_books_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            quantity = quantity - 1;
            Toast.makeText(this, getString(R.string.quantity_change_books_success),
                    Toast.LENGTH_SHORT).show();

            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_QUANTITY_BOOK, quantity);

            Uri updateUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, columnId);

            getContentResolver().update(updateUri, values, null, null);


        }
    }
}

