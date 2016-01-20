package com.loweware.classtodo1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.loweware.classtodo1.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

public class TestUtilities extends AndroidTestCase {
//    static final String TEST_LOCATION = "99705";
//    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

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

//    static ContentValues createDayCycleValues() {
//
//        ContentValues dayCycleValues = new ContentValues();
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "A");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-04");
//        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "B");
//        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-05");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "A");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-06");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "B");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-07");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "A");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-08");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DAY_CYCLE, "N");
////        dayCycleValues.put(CalendarContract.DayCycleEntry.COLUMN_DATE, "2016-01-09");
//
//        return dayCycleValues;
//    }

    static ContentValues createScheduleEntryValues() {
        // Create a new map of values, where column names are the keys
        ContentValues scheduleValues = new ContentValues();
        scheduleValues.put(CalendarContract.ScheduleEntry.COLUMN_ACCOUNT_NAME, "laurenschoolcalendar@gmail.com");
        scheduleValues.put(CalendarContract.ScheduleEntry.COLUMN_CALENDAR_ID, "apps.edina.k12.mn.us_gve4brdetf7t43u5ircivh8qjk@group.calendar.google.com");
        scheduleValues.put(CalendarContract.ScheduleEntry.COLUMN_DAY_CYCLE, "B");
        return scheduleValues;
    }
    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
    static long insertScheduleEntryValues(Context context) {
        // insert our test records into the database
        CalendarDbHelper dbHelper = new CalendarDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = TestUtilities.createScheduleEntryValues();
        long rowId;
        rowId = db.insert(CalendarContract.ScheduleEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert ScheduleEntry Values", rowId != -1);

        return rowId;
    }

//    static ContentValues createCalendarListValues() {
//        // Create a new map of values, where column names are the keys
//        ContentValues calendarListValues = new ContentValues();
//        calendarListValues.put(CalendarContract.CalendarListEntry.COLUMN_ACCOUNT_NAME, "laurenschoolcalendar@gmail.com");
//        calendarListValues.put(CalendarContract.CalendarListEntry.COLUMN_CALENDAR_ID, "apps.edina.k12.mn.us_gve4brdetf7t43u5ircivh8qjk@group.calendar.google.com");
//        //scheduleValues.put(CalendarContract.CalendarListEntry.COLUMN_DESCRIPTION, "Teacher, Ingrid Bakke");
//        calendarListValues.put(CalendarContract.CalendarListEntry.COLUMN_CLASS_NAME, "VV WOM6BAK");
//        return calendarListValues;
//    }

//    static ContentValues createEventEntryValues() {
//        // Create a new map of values, where column names are the keys
//        ContentValues eventValues = new ContentValues();
//        eventValues.put(CalendarContract.EventEntry.COLUMN_ACCOUNT_NAME, "laurenschoolcalendar@gmail.com");
//        eventValues.put(CalendarContract.EventEntry.COLUMN_EVENT_ID, "2jqrqib8g51agsguo7u9hltau4");
//        eventValues.put(CalendarContract.EventEntry.COLUMN_CALENDAR_ID, "apps.edina.k12.mn.us_gve4brdetf7t43u5ircivh8qjk@group.calendar.google.com");
//        eventValues.put(CalendarContract.EventEntry.COLUMN_ENTRIES, "Winter Play workshop");
//        eventValues.put(CalendarContract.EventEntry.COLUMN_DATE, "2016-01-05");
//        return eventValues;
//    }

    /**
        The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
