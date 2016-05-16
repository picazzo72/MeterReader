package com.dandersen.meterreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dandersen on 16-05-2016.
 */
public class MeterDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "meter.db";

    public MeterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_SUPPLIER_TABLE = "CREATE TABLE " + MeterContract.SupplierEntry.TABLE_NAME + " (" +
                MeterContract.SupplierEntry._ID + " INTEGER PRIMARY KEY," +

                // The supplier type
                MeterContract.SupplierEntry.COLUMN_SUPPLIER_TYPE + " INTEGER NOT NULL, " +

                // Supplier name
                MeterContract.SupplierEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                // Homepage
                MeterContract.SupplierEntry.COLUMN_HOMEPAGE + " TEXT, " +

                // To assure the application have just one supplier of the same type,
                // it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MeterContract.SupplierEntry.COLUMN_SUPPLIER_TYPE + ", " +
                MeterContract.SupplierEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_HOUSING_TABLE = "CREATE TABLE " + MeterContract.HousingEntry.TABLE_NAME + " (" +
                MeterContract.HousingEntry._ID + " INTEGER PRIMARY KEY," +

                // Name of owner
                MeterContract.HousingEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                // Address
                MeterContract.HousingEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +

                // City
                MeterContract.HousingEntry.COLUMN_CITY + " TEXT NOT NULL, " +

                // State
                MeterContract.HousingEntry.COLUMN_STATE + " TEXT, " +

                // Country
                MeterContract.HousingEntry.COLUMN_COUNTRY + " TEXT, " +

                // To assure the application have just one housing with same address,
                // it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MeterContract.HousingEntry.COLUMN_ADDRESS + ", " +
                MeterContract.HousingEntry.COLUMN_CITY + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_METER_TABLE = "CREATE TABLE " + MeterContract.MeterEntry.TABLE_NAME + " (" +
                MeterContract.MeterEntry._ID + " INTEGER PRIMARY KEY," +

                // The meter reading
                MeterContract.MeterEntry.COLUMN_METER_READING + " REAL NOT NULL, " +

                // Date, stored as long in milliseconds since the epoch
                MeterContract.MeterEntry.COLUMN_DATE + " INTEGER NOT NULL, " +

                // Note
                MeterContract.MeterEntry.COLUMN_NOTE + " TEXT, " +

                // Supplier id
                MeterContract.MeterEntry.COLUMN_SUPPLIER_ID + " INTEGER NOT NULL, " +

                // Housing id
                MeterContract.MeterEntry.COLUMN_HOUSING_ID + " INTEGER NOT NULL, " +

                // Set up the supplier column as a foreign key to supplier table.
                " FOREIGN KEY (" + MeterContract.MeterEntry.COLUMN_SUPPLIER_ID + ") REFERENCES " +
                MeterContract.SupplierEntry.TABLE_NAME + " (" + MeterContract.SupplierEntry._ID + "), " +

                // Set up the housing column as a foreign key to housing table.
                " FOREIGN KEY (" + MeterContract.MeterEntry.COLUMN_HOUSING_ID + ") REFERENCES " +
                MeterContract.HousingEntry.TABLE_NAME + " (" + MeterContract.HousingEntry._ID + "));";


        sqLiteDatabase.execSQL(SQL_CREATE_SUPPLIER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HOUSING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_METER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        // TODO
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
}
