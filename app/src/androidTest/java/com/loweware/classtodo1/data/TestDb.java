package com.loweware.classtodo1.data;

/**
 * Created by david on 1/6/2016.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        Log.i(LOG_TAG, "TestDb.deleteTheDatabase()");
        mContext.deleteDatabase(CalendarDbHelper.DATABASE_NAME);
    }

    /**
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        Log.i(LOG_TAG, "TestDb.setUp()");
        deleteTheDatabase();
    }

    /**
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

        Log.i(LOG_TAG, "IN - TestDb.testCreateDb()");

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(CalendarContract.ScheduleEntry.TABLE_NAME);
//        tableNameHashSet.add(CalendarContract.DayCycleEntry.TABLE_NAME);
//        tableNameHashSet.add(CalendarContract.EventEntry.TABLE_NAME);

        mContext.deleteDatabase(CalendarDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new CalendarDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + CalendarContract.ScheduleEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        //TODO: Put a switch statement here to account for all the tables
        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> scheduleEntryColumnHashSet = new HashSet<String>();
        scheduleEntryColumnHashSet.add(CalendarContract.ScheduleEntry._ID);
        scheduleEntryColumnHashSet.add(CalendarContract.ScheduleEntry.COLUMN_ACCOUNT_NAME);
        scheduleEntryColumnHashSet.add(CalendarContract.ScheduleEntry.COLUMN_CALENDAR_ID);
        scheduleEntryColumnHashSet.add(CalendarContract.ScheduleEntry.COLUMN_DAY_CYCLE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            scheduleEntryColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                scheduleEntryColumnHashSet.isEmpty());
        db.close();

        Log.i(LOG_TAG, "OUT - TestDb.testCreateDb()");
    }

    /**
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
//    public void testDayCycleTable() {
//        ContentValues values = TestUtilities.createDayCycleValues();
//        tableTest(values, CalendarContract.DayCycleEntry.TABLE_NAME);
//    }

//    public void testCalendarListTable() {
//        ContentValues values = TestUtilities.createCalendarListValues();
//        tableTest(values, CalendarContract.CalendarListEntry.TABLE_NAME);
//    }
//
    public void testScheduleTable() {
        ContentValues values = TestUtilities.createScheduleEntryValues();
        tableTest(values, CalendarContract.ScheduleEntry.TABLE_NAME);
    }
//
//    public void testEventTable() {
//        ContentValues values = TestUtilities.createEventEntryValues();
//        tableTest(values, CalendarContract.EventEntry.TABLE_NAME);
//    }

    private void tableTest(ContentValues values, String tableName) {

        Log.i(LOG_TAG, "IN - TestDb.tableTest(" + tableName + ")");
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        CalendarDbHelper dbHelper = new CalendarDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create dayCycle values
        //ContentValues values = TestUtilities.createScheduleEntryValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long rowId = db.insert(tableName, null, values);
        assertTrue(rowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                tableName,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from " + tableName + " query", cursor.moveToFirst());

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb on " + tableName + " failed to validate",
                cursor, values);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from " + tableName + " query",
                cursor.moveToNext());

        // Sixth Step: Close cursor and database
        cursor.close();
        dbHelper.close();
        Log.i(LOG_TAG, "OUT - TestDb.tableTest(" + tableName + ")");
    }
}
