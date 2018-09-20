package com.example.android.bookinventory;
// Based on Udacity's Pets program: https://github.com/udacity/ud845-Pets

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract.BookEntry;


public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;

    /**
     * Content URI for the existing book (null if it's a new pet)
     */
    private Uri mCurrentBookUri;

    private EditText mNameEditText;

    /**
     * EditText field to enter the book's price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the quantity's book
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the supplier's book
     */
    private EditText mSupplierEditText;

    /**
     * EditText field to enter the phones's supplier's book
     */
    private EditText mPhoneEditText;


    /**
     * Boolean flag that keeps track of whether the book has been edited (true) or not (false)
     */
    private boolean mBookHasChanged = false;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mBookHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };
    private static final int REQUEST_PHONE_CALL = 1;

    //Global variables used with adding and subtracting edit quantity buttons
    boolean buttonPlus = false;
    boolean buttonMinus = false;
    int newQuantityPlus;
    int newQuantityMinus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null) {
            setTitle(getString(R.string.editor_activity_title_add_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a book that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize a loader to read the book data from the database
            // and display the current values in the editor
            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_book_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_book_quantity);
        mSupplierEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);

        //Button action to reduce quantity by 1
        ImageButton minus = findViewById(R.id.minusButton);
        minus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //Check if this is an existing item record
                if (mCurrentBookUri != null) {

                    //This is an existing record so get the current quantity in EditText
                    String quantityString = mQuantityEditText.getText().toString().trim();

                    //Check if quantity is greater than zero to enable reduction
                    if ((Integer.valueOf(quantityString)) > 0) {
                        int quantity = (Integer.valueOf(quantityString)) - 1;

                        //Save the decreased value of quantity in the EditText field
                        ContentValues values2 = new ContentValues();
                        String newStringQuantity = Integer.toString(quantity);
                        mQuantityEditText.setText(newStringQuantity, TextView.BufferType.EDITABLE);
                        values2.put(BookEntry.COLUMN_QUANTITY_BOOK, newStringQuantity);

                    } else {
                        //Quantity is zero and can not be reduced further so alert user with message
                        Toast.makeText(getApplicationContext(), getString(R.string.quantity_change_books_failed),
                                Toast.LENGTH_SHORT).show();
                    }

                }
                //This is a new item record
                else {

                    //Since there is no value in quantity and we pressed the decrease button, put 0 in EditText
                    if (!buttonMinus) {
                        mQuantityEditText.setText("0", TextView.BufferType.EDITABLE);

                        //Reset boolean so we know there is an initial value in quantity
                        buttonMinus = true;

                        //Read value of quantity in EditText and save it to variable newQuantityPlus
                        String quantityString = mQuantityEditText.getText().toString().trim();
                        newQuantityMinus = Integer.valueOf(quantityString);

                        //Alert users there are no items
                        Toast.makeText(getApplicationContext(), getString(R.string.quantity_change_books_failed),
                                Toast.LENGTH_SHORT).show();

                    } else {
                        //There is a value in quantity
                        String quantityString = mQuantityEditText.getText().toString().trim();
                        newQuantityMinus = Integer.valueOf(quantityString);

                        //Check whether quantity in EditText is O and if so, alert users there is no quantity
                        if (newQuantityMinus == 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.quantity_change_books_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //Decrease quantity in new product using the variable and put it in EditText
                            newQuantityMinus = newQuantityMinus - 1;
                            ContentValues values = new ContentValues();
                            String newStringQuantity = Integer.toString(newQuantityMinus);
                            mQuantityEditText.setText(newStringQuantity, TextView.BufferType.EDITABLE);
                            values.put(BookEntry.COLUMN_QUANTITY_BOOK, newStringQuantity);
                        }
                    }

                }
            }

        });
        //Button action to increase quantity by 1
        ImageButton plus = findViewById(R.id.plusButton);
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if this is an existing item record
                if (mCurrentBookUri != null) {

                    //Get existing quantity and increase it by 1
                    String quantityString = mQuantityEditText.getText().toString().trim();
                    int quantity = (Integer.valueOf(quantityString)) + 1;

                    //Save the increased value of quantity in EditText field
                    ContentValues values2 = new ContentValues();
                    String newStringQuantity = Integer.toString(quantity);
                    mQuantityEditText.setText(newStringQuantity, TextView.BufferType.EDITABLE);
                    values2.put(BookEntry.COLUMN_QUANTITY_BOOK, newStringQuantity);
                }
                //This is a new item record
                else {
                    //Since there is no value in quantity and we pressed the increase button, put 1 in EditText
                    if (!buttonPlus) {
                        mQuantityEditText.setText("1", TextView.BufferType.EDITABLE);

                        //Reset boolean so we know there is an initial value in quantity
                        buttonPlus = true;

                        //Read value of quantity in EditText and save it to variable newQuantityPlus
                        String quantityString = mQuantityEditText.getText().toString().trim();
                        newQuantityPlus = Integer.valueOf(quantityString);
                    } else {

                        //Increase quantity in new product using the variable and put it in EditText
                        newQuantityPlus = newQuantityPlus + 1;
                        ContentValues values = new ContentValues();
                        String newStringQuantity = Integer.toString(newQuantityPlus);
                        mQuantityEditText.setText(newStringQuantity, TextView.BufferType.EDITABLE);
                        values.put(BookEntry.COLUMN_QUANTITY_BOOK, newStringQuantity);
                    }
                }
            }
        });

        //Call supplier using phone ImageButton
        ImageButton phoneButton = findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            EditorActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PHONE_CALL);
                    return;
                }

                String number = mPhoneEditText.getText().toString().trim();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                getApplicationContext().startActivity(callIntent);
            }
        });
    }


    /**
     * Get user input from editor and save book into database.
     */
    private void saveBook() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();

        //Check if this is supposed to be a NEW inventory item
        // AND any field is NULL - do not save
        if (mCurrentBookUri == null &&
                (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                        TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierString) ||
                        TextUtils.isEmpty(phoneString))) {

            // Since no fields were modified, we can return early without creating a new book.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            Toast.makeText(this, getString(R.string.editor_saved_book_failed),
                    Toast.LENGTH_SHORT).show();

            getApplicationContext();
            Intent i = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(i);
            return;

        }

        //Validation for existing Inventory item and values must be not null
        if (mCurrentBookUri != null && TextUtils.isEmpty(nameString) ||
                TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(supplierString) ||
                TextUtils.isEmpty(phoneString)) {

            // Since value(s) are null prompt toast
            Toast.makeText(this, getString(R.string.editor_update_book_failed),
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
            return;

        }
        //Set price to 0 if it is empty or only equal "."
        int priceZero = 0;
        if (TextUtils.isEmpty(priceString) || priceString.matches("") || priceString.equals(".")) {
            priceString = Integer.toString(priceZero);
        }

        //Set quantity to 0 if it is empty
        int quantityZero = 0;
        if (TextUtils.isEmpty(quantityString) || quantityString.matches("")) {
            quantityString = Integer.toString(quantityZero);
        }


        //Validate phone entry can be null but if not empty must have 10 numbers
        if (!TextUtils.isEmpty(phoneString) && (phoneString.length() != 10)) {
            Toast.makeText(this, getString(R.string.phone_save_failed),
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        } else {
            // Create a ContentValues object where column names are the keys,
            // and inventory attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
            values.put(BookEntry.COLUMN_PRICE_BOOK, priceString);
            values.put(BookEntry.COLUMN_QUANTITY_BOOK, quantityString);
            values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplierString);
            values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, phoneString);


            // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not
            if (mCurrentBookUri == null) {
                // This is a NEW book, so insert a new book into the provider,
                // returning the content URI for the new book.
                Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_saved_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_saved_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
                // and pass in the new ContentValues. Pass in null for the selection and selection args
                // because mCurrentBookUri will already identify the correct row in the database that
                // we want to modify.

                int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveBook();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link BookActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the book table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_PRICE_BOOK,
                BookEntry.COLUMN_QUANTITY_BOOK,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current pet  projection,
                projection,               // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {

            // Find the columns of books attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE_BOOK);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY_BOOK);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);
            int price = cursor.getInt(priceColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mSupplierEditText.setText(supplier);
            mPhoneEditText.setText(phone);
            mQuantityEditText.setText(String.valueOf(quantity));
            mPriceEditText.setText(String.valueOf(price));

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this book.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentBookUri
            // content URI already identifies the book that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }
}

