package com.loweware.classtodo1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/9/2016.
 */
public class FetchCalendarDataTask extends AsyncTask<String, Void, List<String>> {

    public final String LOG_TAG = FetchCalendarDataTask.class.getSimpleName();
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private int mDuration;
    private String mEventQuery = "*";
    private ArrayAdapter<String> mForecastAdapter;
    private String mUserAccountName = "";
    private final Context mContext;
    ArrayList mErrors = new ArrayList();


    public FetchCalendarDataTask(GoogleAccountCredential credential, Context context, ArrayAdapter<String> forecastAdapter) {

        Log.i(LOG_TAG, "IN FetchCalendarDataTask");

        mContext = context;
        mForecastAdapter = forecastAdapter;
        //mEventQuery = query;
        mUserAccountName = credential.getSelectedAccountName();

        Log.i(LOG_TAG, mUserAccountName);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        Log.i(LOG_TAG, "OUT FetchCalendarDataTask");
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(String... params) {

        mEventQuery = params[0];
        mDuration = Integer.parseInt(params[1]);

        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        mForecastAdapter.clear();
    }

    @Override
    protected void onPostExecute(List<String> output) {

        Log.i(LOG_TAG, "IN onPostExecute");
        if (output == null || output.size() == 0) {
            mErrors.add("No results returned.");
        } else {
            for(String dayForecastStr : output) {
                //Log.i(LOG_TAG, "Output: " + dayForecastStr);
                mForecastAdapter.add(dayForecastStr);
            }
        }
        Log.i(LOG_TAG, "OUT onPostExecute");
    }

    @Override
    protected void onCancelled() {

        Log.i(LOG_TAG, "IN onCancelled");
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                mErrors.add(mLastError.toString());

            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                mErrors.add("UserRecoverableAuthIOException.");
            } else {
                mErrors.add(mLastError.toString());
            }
        } else {
            mErrors.add("Request Cancelled.");
        }
        Log.i(LOG_TAG, "OUT onCancelled");
    }

    private List<String> getDataFromApi() throws IOException {

        Log.i(LOG_TAG, "IN getDataFromApi");
        //List<String> eventStrings = getEventsByCalendarId("apps.edina.k12.mn.us_45hcc88tr8ds5m6h1grloacuak@group.calendar.google.com");
        List<String> eventStrings = getEventsByCalendarId("enaaj9tnb8tme3ca5cb8a17iq1lu5dbv@import.calendar.google.com");
        //List<String> eventStrings = getCalendars();//"enaaj9tnb8tme3ca5cb8a17iq1lu5dbv@import.calendar.google.com");
        Log.i(LOG_TAG, "OUT getDataFromApi");
        return eventStrings;
    }

    //Utility function. Get a list of events in calendar with calendarId
    private List<String> getEventsByCalendarId(String calendarId) throws IOException {
        // List the next 10 events from the primary calendar.
        Log.i(LOG_TAG, "IN getEventsByCalendarId");

        DateTime timeMin = new DateTime(System.currentTimeMillis());
        DateTime timeMax = new DateTime(timeMin.getValue() + (1000 * 60 * 60 * 24 * mDuration));

        List<String> eventStrings = new ArrayList<>();
        Events events = mService.events().list(calendarId)
                .setMaxResults(10)
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setFields("kind,items(id,description, summary, start/date,end/date)")
                .set("q", mEventQuery)
                .execute();

        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            DateTime endDate = event.getEnd().getDateTime();
            if (start == null) { // All-day events don't have start times, so just use the start date.
                start = event.getStart().getDate();
            }
            if (endDate == null) { // All-day events don't have end times, so just use the end date.
                //endDate = event.getEnd().getDate();
            }
            Log.i(LOG_TAG, "In Loop " + event.getSummary());
            eventStrings.add(String.format("(%s) %s - %s ", start, event.getSummary(), event.getDescription()));
            //eventStrings.add("");
        }
        Log.i(LOG_TAG, "OUT getEventsByCalendarId");
        return eventStrings;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     *
     * @return List of Strings describing returned events.
     * @throws IOException Get a list of calendars where selected=true
     */
    private List<String> getCalendars() throws IOException {
        // List the next 10 events from the primary calendar.
        Log.i(LOG_TAG, "Enter getDataFromApi");
        List<String> calStrings = new ArrayList<>();
        String calSummary;

        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            CalendarList calList = mService.calendarList().list()
                    .setPageToken(pageToken)
                    .execute();

            List<CalendarListEntry> items = calList.getItems();

            for (CalendarListEntry calListEntry : items) {
                if (calListEntry.getSummaryOverride() == null) {
                    calSummary = calListEntry.getSummary();
                } else {
                    calSummary = calListEntry.getSummaryOverride();
                }
                calStrings.add(String.format("%s (%s)", calSummary, calListEntry.getId()));
            }
            pageToken = calList.getNextPageToken();

        } while (pageToken != null);

        return calStrings;
    }

    //Get a list of events in all calendars
    private List<String> getEventsAll(List<String> calendars) throws IOException {
        List<String> eventStrings = new ArrayList<>();
        return eventStrings;
    }

}