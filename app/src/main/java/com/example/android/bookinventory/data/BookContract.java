package com.example.android.bookinventory.data;

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
    public static final String CONTENT_AUTHORITY = "com.example.android.bookInventory";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
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
         * Genre of the book.
         * The only possible values are {@link #GENRE_UNKNOWN}, {@link #GENRE_ACTION}, {@link #GENRE_COMEDY},
         * {@link #GENRE_ROMANCE}, {@link #GENRE_FICTION}.
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_GENRE = "genre";

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
        /**
         * Possible values for the genre of the book.
         */
        public static final int GENRE_ACTION = 1;
        public static final int GENRE_COMEDY = 2;
        public static final int GENRE_ROMANCE = 3;
        public static final int GENRE_FICTION = 4;
        public static final int GENRE_UNKNOWN = 0;

        public static boolean isValidGenre(Integer genre) {
            if (genre == GENRE_UNKNOWN || genre == GENRE_FICTION || genre == GENRE_COMEDY ||
                    genre == GENRE_ROMANCE || genre == GENRE_ACTION) {
                return true;
            }
            return false;
        }
    }
}


