package com.loweware.classtodo1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by david on 1/5/2016.
 */
public class CalendarContract {

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    public static final String CONTENT_AUTHORITY = "com.loweware.classtodo1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Day cycles (A/B days) for the current forecast duration:
    public static final String PATH_DAY_CYCLE = "days";
    //List of all calendars associated to the current credentials
    public static final String PATH_CALENDAR_LIST = "calendars";
    //Table with A/B day cycle column
    public static final String PATH_SCHEDULE = "schedule";
    //Events from all calendars for the current forecast duration:
    public static final String PATH_FORECAST_EVENTS = "events";
    //Events from all calendars for the current forecast duration
    //matching a query:
    public static final String PATH_SEARCH_EVENT = "search";

    public static final String PATH_EVENT_DETAIL = "detail";

//    public static final class DayCycleEntry implements BaseColumns {
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAY_CYCLE).build();
//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAY_CYCLE;
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAY_CYCLE;
//
//        public static final String TABLE_NAME = "dayCycle";
//        public static final String COLUMN_DAY_CYCLE = "cycle";
//        public static final String COLUMN_DATE = "date";
//
//        public static Uri buildCalendarUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }

//    public static final class CalendarListEntry implements BaseColumns {
//        //https://www.googleapis.com/calendar/v3/users/me/calendarList
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CALENDAR_LIST).build();
//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CALENDAR_LIST;
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CALENDAR_LIST;
//
//        public static final String TABLE_NAME = "classSchedule";
//        public static final String COLUMN_ACCOUNT_NAME = "uid";
//        public static final String COLUMN_CALENDAR_ID = "calendarId";
//        public static final String COLUMN_CLASS_NAME = "className";
//
//        public static Uri buildAccountName(String accountName) {
//            return CONTENT_URI.buildUpon().appendPath(accountName).build();
//        }
//
//        public static Uri buildCalendarUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }



    public static final class ScheduleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCHEDULE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;

        public static final String TABLE_NAME = "classSchedule";
        public static final String COLUMN_ACCOUNT_NAME = "uid";
        public static final String COLUMN_CALENDAR_ID = "calendarId";
        public static final String COLUMN_DAY_CYCLE = "cycle";

        public static Uri buildCalendarUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

//    public static final class EventEntry implements BaseColumns {
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORECAST_EVENTS).build();
//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_EVENTS;
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST_EVENTS;
//
//        public static final String TABLE_NAME = "classEvents";
//        public static final String COLUMN_ACCOUNT_NAME = "uid";
//        public static final String COLUMN_EVENT_ID = "eventId";
//        public static final String COLUMN_CALENDAR_ID = "calendarId";
//        public static final String COLUMN_ENTRIES = "entries";
//        public static final String COLUMN_DATE = "date";
//
//        public static Uri buildCalendarUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

//        public static Uri buildEvntWithStartDate(String locationSetting, String startDate) {
//            //long normalizedDate = normalizeDate(startDate);
//            return CONTENT_URI.buildUpon().appendPath(locationSetting)
//                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
//        }
//    }

}
