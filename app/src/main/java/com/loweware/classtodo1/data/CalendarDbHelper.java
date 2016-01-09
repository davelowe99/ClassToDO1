package com.loweware.classtodo1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loweware.classtodo1.data.CalendarContract.ScheduleEntry;
import com.loweware.classtodo1.data.CalendarContract.DayCycleEntry;
import com.loweware.classtodo1.data.CalendarContract.EventEntry;

/**
 * Created by david on 1/5/2016.
 */
public class CalendarDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "schoolcal.db";

    public static final String LOG_TAG = CalendarDbHelper.class.getSimpleName();

    public CalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i(LOG_TAG, "IN - CalendarDbHelper.onCreate()");
        MakeTable_DayCycleEntry(sqLiteDatabase);
        MakeTable_ScheduleEntry(sqLiteDatabase);
        MakeTable_EventEntry(sqLiteDatabase);
        Log.i(LOG_TAG, "OUT - CalendarDbHelper.onCreate()");
    }

    private void MakeTable_DayCycleEntry(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG_TAG, "IN - CalendarDbHelper.MakeTable_DayCycleEntry()");
        final String SQL_CREATE_DAYCYCLE_TABLE = "CREATE TABLE " + DayCycleEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                DayCycleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DayCycleEntry.COLUMN_DAY_CYCLE + " TEXT NOT NULL, " +
                DayCycleEntry.COLUMN_DATE + " TEXT NOT NULL" + ");";
        Log.i(LOG_TAG, "SQL - " + SQL_CREATE_DAYCYCLE_TABLE );
        sqLiteDatabase.execSQL(SQL_CREATE_DAYCYCLE_TABLE);
        Log.i(LOG_TAG, "OUT - CalendarDbHelper.MakeTable_DayCycleEntry()");
    }

    private void MakeTable_ScheduleEntry(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_SCHEDULE_TABLE = "CREATE TABLE " + ScheduleEntry.TABLE_NAME + " (" +
                ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ScheduleEntry.COLUMN_CALENDAR_ID + " TEXT NOT NULL, " +
                ScheduleEntry.COLUMN_DAY_CYCLE + " TEXT NOT NULL, " +
                ScheduleEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                ScheduleEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ScheduleEntry.COLUMN_SUMMARY_OVERRIDE + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SCHEDULE_TABLE);
    }

    private void MakeTable_EventEntry(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +

                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EventEntry.COLUMN_EVENT_ID + " TEXT NOT NULL, " +
                EventEntry.COLUMN_CALENDAR_ID + " TEXT NOT NULL, " +
                EventEntry.COLUMN_ENTRIES + " TEXT NOT NULL, " +
                EventEntry.COLUMN_DATE + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DayCycleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScheduleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
