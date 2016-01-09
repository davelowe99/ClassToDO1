package com.loweware.classtodo1.data;

import android.provider.BaseColumns;

/**
 * Created by david on 1/5/2016.
 */
public class CalendarContract {

    public static final class DayCycleEntry implements BaseColumns {
        public static final String TABLE_NAME = "dayCycle";

        public static final String COLUMN_DAY_CYCLE = "cycle";
        public static final String COLUMN_DATE = "date";
    }

    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "classEvents";

        public static final String COLUMN_EVENT_ID = "eventId";
        public static final String COLUMN_CALENDAR_ID = "calendarId";
        public static final String COLUMN_ENTRIES = "entries";
        public static final String COLUMN_DATE = "date";
    }

    public static final class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "classSchedule";
        public static final String COLUMN_CALENDAR_ID = "calendarId";
        public static final String COLUMN_DAY_CYCLE = "cycle";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SUMMARY_OVERRIDE = "summaryOverride";
    }
}
