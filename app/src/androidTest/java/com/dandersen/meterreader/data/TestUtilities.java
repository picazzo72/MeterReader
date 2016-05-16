package com.dandersen.meterreader.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by dandersen on 16-05-2016.
 */
public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createSantaClausSupplierValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MeterContract.SupplierEntry.COLUMN_SUPPLIER_TYPE, 1);
        testValues.put(MeterContract.SupplierEntry.COLUMN_NAME, "Santa's Secret Village");
        testValues.put(MeterContract.SupplierEntry.COLUMN_HOMEPAGE, "http://www.northpole.com/");

        return testValues;
    }

    static ContentValues createDonaldDuckHousingValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MeterContract.HousingEntry.COLUMN_NAME, "Donald Duck");
        testValues.put(MeterContract.HousingEntry.COLUMN_ADDRESS, "1313 Webfoot Walk");
        testValues.put(MeterContract.HousingEntry.COLUMN_CITY, "Duckburg");
        testValues.put(MeterContract.HousingEntry.COLUMN_STATE, "Calisota");
        testValues.put(MeterContract.HousingEntry.COLUMN_COUNTRY, "USA");

        return testValues;
    }

    static ContentValues createMeterValues(long supplierRowId, long housingRowId) {
        ContentValues meterValues = new ContentValues();
        meterValues.put(MeterContract.MeterEntry.COLUMN_SUPPLIER_ID, supplierRowId);
        meterValues.put(MeterContract.MeterEntry.COLUMN_HOUSING_ID, housingRowId);
        meterValues.put(MeterContract.MeterEntry.COLUMN_METER_READING, 75000.6);
        meterValues.put(MeterContract.MeterEntry.COLUMN_DATE, TEST_DATE);
        meterValues.put(MeterContract.MeterEntry.COLUMN_NOTE, "Test reading");

        return meterValues;
    }

}
