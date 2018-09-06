package com.example.android.bookinventory.data;

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
     * Inner class that defines constant values for the book database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "Books";

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
        public static final int GENRE_ACTION = 0;
        public static final int GENRE_COMEDY = 1;
        public static final int GENRE_ROMANCE = 2;
        public static final int GENRE_FICTION = 3;
        public static final int GENRE_UNKNOWN = 4;
    }
}


