package com.loweware.classtodo1;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 12/29/2015.
 */
public class ForecastFragment extends Fragment {

    ArrayAdapter<String> mForecastAdapter;
    int FORECAST_DAYS = 14;  // TODO: 12/3/2015  - CREATE USER SETTING FOR FORECAST_DAYS
    String mEventQuery = "*";// TODO: 12/3/2015 - CREATE EVENT QUERY TEXTBOX

    public ProgressBar mSpinner;


    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchDataTask weatherTask = new FetchDataTask(MainActivity.mCredential);
            //FetchDataTask weatherTask = new FetchDataTask(((MainActivity)getActivity()).mCredential);
            weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {"Hello",
                         "World"};

        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));

        mForecastAdapter =
                new ArrayAdapter<>(
                        getActivity(),                      // The current context (this activity)
                        R.layout.list_item_forecast,        // The name of the layout ID.
                        R.id.list_item_forecast_textview,   // The ID of the textview to populate.
                        weekForecast);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSpinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        mSpinner.setVisibility(View.GONE);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    public class FetchDataTask extends AsyncTask<Void, Void, List<String>> {

        public final String LOG_TAG = FetchDataTask.class.getSimpleName();
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public FetchDataTask(GoogleAccountCredential credential) {

            Log.i(LOG_TAG, "IN FetchDataTask");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {

            Log.i(LOG_TAG, "IN getDataFromApi");
            //List<String> eventStrings = getEventsByCalendarId("apps.edina.k12.mn.us_45hcc88tr8ds5m6h1grloacuak@group.calendar.google.com");
            List<String> eventStrings = getEventsByCalendarId("enaaj9tnb8tme3ca5cb8a17iq1lu5dbv@import.calendar.google.com");
            //List<String> eventStrings = getCalendars();//"enaaj9tnb8tme3ca5cb8a17iq1lu5dbv@import.calendar.google.com");
            Log.i(LOG_TAG, "OUT getDataFromApi");
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

        //Utility function. Get a list of events in calendar with calendarId
        private List<String> getEventsByCalendarId(String calendarId) throws IOException {
            // List the next 10 events from the primary calendar.
            Log.i(LOG_TAG, "IN getEventsByCalendarId");

            DateTime timeMin = new DateTime(System.currentTimeMillis());
            DateTime timeMax = new DateTime(timeMin.getValue() + (1000 * 60 * 60 * 24 * FORECAST_DAYS));

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

        @Override
        protected void onPreExecute() {

            mForecastAdapter.clear();
            mSpinner.setVisibility(View.VISIBLE);
            //mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            // mProgress.hide();
            mSpinner.setVisibility(View.GONE);
            Log.i(LOG_TAG, "IN onPostExecute");
            if (output == null || output.size() == 0) {
                Toast.makeText(getActivity(), "No results returned.", Toast.LENGTH_LONG).show();
            } else {
                for(String dayForecastStr : output) {
                    Log.i(LOG_TAG, "Output: " + dayForecastStr);
                    mForecastAdapter.add(dayForecastStr);
                }
            }
            Log.i(LOG_TAG, "OUT onPostExecute");
        }

        @Override
        protected void onCancelled() {
            // mProgress.hide();
            mSpinner.setVisibility(View.GONE);
            Log.i(LOG_TAG, "IN onCancelled");
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    ((MainActivity)getActivity()).showGooglePlayServicesAvailabilityErrorDialog(
//                            ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                    .getConnectionStatusCode());
                    Toast.makeText(getActivity(), mLastError.toString(), Toast.LENGTH_LONG).show();

                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getActivity(), "The following error occurred:\n" + mLastError.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Request cancelled.", Toast.LENGTH_LONG).show();
            }
            Log.i(LOG_TAG, "OUT onCancelled");
        }
    }
}