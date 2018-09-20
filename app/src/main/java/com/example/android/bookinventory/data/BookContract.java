package com.example.android.bookinventory.data;
// Based on Udacity's Pets program: https://github.com/udacity/ud845-Pets

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Books app.
 */

public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.bookinventory";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.books/books/ is a valid path for
     * looking at book data. content://com.example.android.books/books/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "books".
     */
    public static final String PATH_BOOKS = "books";

    /**
     * Inner class that defines constant values for the book database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the books(only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_NAME = "name";


        /**
         * Price of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE_BOOK = "price";
        /**
         * Quantity of books.
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY_BOOK = "quantity";

        /**
         * Supplier Name.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier";

        /**
         * Supplier Phone Number.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "phone";

    }
}



