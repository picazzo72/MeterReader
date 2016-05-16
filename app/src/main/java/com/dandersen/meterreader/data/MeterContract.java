package com.dandersen.meterreader.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import com.dandersen.meterreader.BuildConfig;

import java.util.Date;

/**
 * Created by dandersen on 16-05-2016.
 */
public class MeterContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.dandersen.meterreader/meter/ is a valid path for
    // looking at meter data. content://com.dandersen.meterreader/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    public static final String PATH_METER = "meter";
    public static final String PATH_SUPPLIER = "supplier";
    public static final String PATH_HOUSING = "housing";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the meter table */
    public static final class MeterEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_METER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_METER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_METER;

        // Table name
        public static final String TABLE_NAME = PATH_METER;

        // The meter reading
        public static final String COLUMN_METER_READING = "meter_reading";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";

        // Note
        public static final String COLUMN_NOTE = "note";

        // Supplier id
        public static final String COLUMN_SUPPLIER_ID = "supplier_id";

        // Housing id
        public static final String COLUMN_HOUSING_ID = "housing_id";

        public static Uri buildMeterUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the supplier table */
    public static final class SupplierEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUPPLIER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIER;

        // Table name
        public static final String TABLE_NAME = PATH_SUPPLIER;

        // The supplier type (heat/power/water/gas)
        public static final String COLUMN_SUPPLIER_TYPE = "supplier_type";

        // Name of supplier
        public static final String COLUMN_NAME = "name";

        // Url to supplier web page
        public static final String COLUMN_HOMEPAGE = "homepage";

        public static Uri buildSupplierUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the house table */
    public static final class HousingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOUSING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOUSING;

        // Table name
        public static final String TABLE_NAME = PATH_HOUSING;

        // Name of owner
        public static final String COLUMN_NAME = "name";

        // Address
        public static final String COLUMN_ADDRESS = "address";

        // City
        public static final String COLUMN_CITY = "city";

        // State
        public static final String COLUMN_STATE = "state";

        // Country
        public static final String COLUMN_COUNTRY = "country";

        public static Uri buildHousingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
