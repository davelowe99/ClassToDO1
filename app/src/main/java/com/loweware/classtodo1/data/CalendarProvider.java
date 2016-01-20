package com.loweware.classtodo1.data;
import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
/**
 * Created by david on 1/13/2016.
 */
public class CalendarProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CalendarDbHelper mOpenHelper;

    static final int CALENDAR_LIST_ENTRY = 100;
    static final int DAYCYCLE_ENTRY = 101;
    static final int SCHEDULE_ENTRY = 102;
    static final int EVENT_ENTRY = 103;

    static final int SCHEDULE_EVENT = 300;


//    private static final SQLiteQueryBuilder sScheduleEventQueryBuilder;
//    static{
//        sScheduleEventQueryBuilder = new SQLiteQueryBuilder();
//        sScheduleEventQueryBuilder.setTables(
//                CalendarContract.CalendarListEntry.TABLE_NAME +
//                        " INNER JOIN (" + CalendarContract.ScheduleEntry.TABLE_NAME +
//                        " INNER JOIN " + CalendarContract.EventEntry.TABLE_NAME +
//                        " ON " + CalendarContract.ScheduleEntry.COLUMN_CALENDAR_ID +
//                        " = " + CalendarContract.EventEntry.COLUMN_CALENDAR_ID +
//                        ") ON " + CalendarContract.CalendarListEntry.COLUMN_CALENDAR_ID +
//                        " = " + CalendarContract.ScheduleEntry.COLUMN_CALENDAR_ID);
//                //" ORDER BY " + CalendarContract.EventEntry.COLUMN_DATE, CalendarContract.ScheduleEntry.COLUMN_DAY_CYCLE, CalendarContract.EventEntry.COLUMN_ENTRIES);
//    }



    //days = ?
//    private static final String sDayCycle = CalendarContract.DayCycleEntry.TABLE_NAME;

//                    "." + CalendarContract.DayCycleEntry.COLUMN_DATE + " = ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " >= ? ";

//    //location.location_setting = ? AND date >= ?
//    private static final String sLocationSettingWithStartDateSelection =
//            WeatherContract.LocationEntry.TABLE_NAME+
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " >= ? ";
//
//    //location.location_setting = ? AND date = ?
//    private static final String sLocationSettingAndDaySelection =
//            WeatherContract.LocationEntry.TABLE_NAME +
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";

//    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
//        long startDate = WeatherContract.WeatherEntry.getStartDateFromUri(uri);
//
//        String[] selectionArgs;
//        String selection;
//
//        if (startDate == 0) {
//            selection = sLocationSettingSelection;
//            selectionArgs = new String[]{locationSetting};
//        } else {
//            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
//            selection = sLocationSettingWithStartDateSelection;
//        }
//
//        return sScheduleEventQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//    }

//    private Cursor getWeatherByLocationSettingAndDate(
//            Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
//        long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
//
//        return sScheduleEventQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                sLocationSettingAndDaySelection,
//                new String[]{locationSetting, Long.toString(date)},
//                null,
//                null,
//                sortOrder
//        );
//    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.


        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.


        // 3) Return the new matcher!
        return null;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CalendarDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
//            case DAYCYCLE_ENTRY:
//                return CalendarContract.DayCycleEntry.CONTENT_TYPE;
//            case CALENDAR_LIST_ENTRY:
//                return CalendarContract.CalendarListEntry.CONTENT_TYPE;
            case SCHEDULE_ENTRY:
                return CalendarContract.ScheduleEntry.CONTENT_TYPE;
//            case EVENT_ENTRY:
//                return CalendarContract.EventEntry.CONTENT_TYPE;
//            case SCHEDULE_EVENT:
//                return CalendarContract.EventEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "days/14?date=2016-01-14"
//            case DAYCYCLE_ENTRY: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        CalendarContract.DayCycleEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//            // "calendars/uid"
//            case CALENDAR_LIST_ENTRY:
//            {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        CalendarContract.CalendarListEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
            // "schedule/uid"
            case SCHEDULE_ENTRY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CalendarContract.ScheduleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
//            // "event/uid//14?date=2016-01-14"
//            case EVENT_ENTRY: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        CalendarContract.EventEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//            case SCHEDULE_EVENT: {
//                retCursor = null;
//                break;
//            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SCHEDULE_ENTRY: {
                //normalizeDate(values);
                long _id = db.insert(CalendarContract.ScheduleEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CalendarContract.ScheduleEntry.buildCalendarUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.

        // Student: return the actual rows deleted
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SCHEDULE_ENTRY:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(CalendarContract.ScheduleEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    /**
     You do not need to call this method. This is a method specifically to assist the testing
     framework in running smoothly. You can read more at:
     http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
     */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
