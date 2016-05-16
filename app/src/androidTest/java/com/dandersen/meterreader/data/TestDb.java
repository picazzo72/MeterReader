package com.dandersen.meterreader.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by dandersen on 16-05-2016.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MeterDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MeterContract.HousingEntry.TABLE_NAME);
        tableNameHashSet.add(MeterContract.SupplierEntry.TABLE_NAME);
        tableNameHashSet.add(MeterContract.MeterEntry.TABLE_NAME);

        mContext.deleteDatabase(MeterDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MeterDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // check that all tables are created
        assertTrue("Error: Your database was created without one or more of the three required tables",
                tableNameHashSet.isEmpty());

        {
            // now, do our tables contain the correct columns?
            c = db.rawQuery("PRAGMA table_info(" + MeterContract.MeterEntry.TABLE_NAME + ")",
                    null);

            assertTrue("Error: This means that we were unable to query the database for meter table information.",
                    c.moveToFirst());

            // Build a HashSet of all of the column names we want to look for
            final HashSet<String> columnHashSet = new HashSet<String>();
            columnHashSet.add(MeterContract.MeterEntry._ID);
            columnHashSet.add(MeterContract.MeterEntry.COLUMN_METER_READING);
            columnHashSet.add(MeterContract.MeterEntry.COLUMN_DATE);
            columnHashSet.add(MeterContract.MeterEntry.COLUMN_NOTE);
            columnHashSet.add(MeterContract.MeterEntry.COLUMN_SUPPLIER_ID);
            columnHashSet.add(MeterContract.MeterEntry.COLUMN_HOUSING_ID);

            int columnNameIndex = c.getColumnIndex("name");

            do {
                String columnName = c.getString(columnNameIndex);
                columnHashSet.remove(columnName);
            } while (c.moveToNext());

            // if this fails, it means that your database doesn't contain all of the required location
            // entry columns
            assertTrue("Error: The database doesn't contain all of the required meter entry columns",
                    columnHashSet.isEmpty());
        }

        {
            c = db.rawQuery("PRAGMA table_info(" + MeterContract.SupplierEntry.TABLE_NAME + ")",
                    null);

            assertTrue("Error: This means that we were unable to query the database for supplier table information.",
                    c.moveToFirst());

            // Build a HashSet of all of the column names we want to look for
            final HashSet<String> columnHashSet = new HashSet<String>();
            columnHashSet.add(MeterContract.SupplierEntry._ID);
            columnHashSet.add(MeterContract.SupplierEntry.COLUMN_SUPPLIER_TYPE);
            columnHashSet.add(MeterContract.SupplierEntry.COLUMN_NAME);
            columnHashSet.add(MeterContract.SupplierEntry.COLUMN_HOMEPAGE);

            int columnNameIndex = c.getColumnIndex("name");

            do {
                String columnName = c.getString(columnNameIndex);
                columnHashSet.remove(columnName);
            } while (c.moveToNext());

            // if this fails, it means that your database doesn't contain all of the required location
            // entry columns
            assertTrue("Error: The database doesn't contain all of the required supplier entry columns",
                    columnHashSet.isEmpty());
        }

        {
            c = db.rawQuery("PRAGMA table_info(" + MeterContract.HousingEntry.TABLE_NAME + ")",
                    null);

            assertTrue("Error: This means that we were unable to query the database for housing table information.",
                    c.moveToFirst());

            // Build a HashSet of all of the column names we want to look for
            final HashSet<String> columnHashSet = new HashSet<String>();
            columnHashSet.add(MeterContract.HousingEntry._ID);
            columnHashSet.add(MeterContract.HousingEntry.COLUMN_NAME);
            columnHashSet.add(MeterContract.HousingEntry.COLUMN_ADDRESS);
            columnHashSet.add(MeterContract.HousingEntry.COLUMN_CITY);

            int columnNameIndex = c.getColumnIndex("name");

            do {
                String columnName = c.getString(columnNameIndex);
                columnHashSet.remove(columnName);
            } while (c.moveToNext());

            // if this fails, it means that your database doesn't contain all of the required location
            // entry columns
            assertTrue("Error: The database doesn't contain all of the required housing entry columns",
                    columnHashSet.isEmpty());
        }

        db.close();
    }

    private long insertSupplier(SQLiteDatabase db) {
        // Create ContentValues of what you want to insert
        ContentValues contentValues = TestUtilities.createSantaClausSupplierValues();

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(MeterContract.SupplierEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Database insert of supplier row failed: " + Long.toString(rowId), rowId > 0);

        // Query the database and receive a Cursor back
        Cursor c = db.query(MeterContract.SupplierEntry.TABLE_NAME,
                null,
                MeterContract.SupplierEntry._ID + " = ?",
                new String[]{ Long.toString(rowId) },
                null, null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: No records returned from supplier query", c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Supplier values are not correct", c, contentValues);

        // Finally, close the cursor
        c.close();

        return rowId;
    }

    /*
        Test that we can insert and query the housing table.
    */
    public void testSupplierTable() {
        // First step: Get reference to writable database
        SQLiteDatabase db = new MeterDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Insert ContentValues into database and get a row ID back
        insertSupplier(db);

        // Finally, close the database
        db.close();
    }

    private long insertHousing(SQLiteDatabase db) {
        // Create ContentValues of what you want to insert
        ContentValues contentValues = TestUtilities.createDonaldDuckHousingValues();

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(MeterContract.HousingEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Database insert of housing row failed: " + Long.toString(rowId), rowId > 0);

        // Query the database and receive a Cursor back
        Cursor c = db.query(MeterContract.HousingEntry.TABLE_NAME,
                null,
                MeterContract.HousingEntry._ID + " = ?",
                new String[]{ Long.toString(rowId) },
                null, null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: No records returned from housing query", c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Housing values are not correct", c, contentValues);

        // Finally, close the cursor
        c.close();

        return rowId;
    }

    /*
        Test that we can insert and query the housing table.
    */
    public void testHousingTable() {
        // First step: Get reference to writable database
        SQLiteDatabase db = new MeterDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Insert ContentValues into database and get a row ID back
        insertHousing(db);

        // Finally, close the database
        db.close();
    }

    /*
        Test that we can insert and query the meter table
     */
    public void testMeterTable() {
        // First step: Get reference to writable database
        SQLiteDatabase db = new MeterDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Insert supplier into database and get a row ID back
        long supplierId = insertSupplier(db);

        // Insert housing into database and get a row ID back
        long housingId = insertHousing(db);

        // Create ContentValues of what you want to insert
        ContentValues contentValues = TestUtilities.createMeterValues(supplierId, housingId);

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(MeterContract.MeterEntry.TABLE_NAME, null, contentValues);
        assertTrue("Error: Database insert of meter entry failed: " + Long.toString(rowId), rowId > 0);

        // Query the database and receive a Cursor back
        Cursor c = db.query(MeterContract.MeterEntry.TABLE_NAME,
                null,
                MeterContract.MeterEntry._ID + " = ?",
                new String[]{ Long.toString(rowId) },
                null, null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: No records returned from meter query", c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Meter entry values are not correct", c, contentValues);

        // Finally, close the cursor and database
        c.close();
        db.close();
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        return -1L;
    }
}
