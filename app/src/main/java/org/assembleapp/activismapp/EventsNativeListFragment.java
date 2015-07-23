package org.assembleapp.activismapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.assembleapp.activismapp.dummy.DummyContent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class EventsNativeListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String LOG_TAG = EventsNativeListFragment.class.getSimpleName();

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    private String zipcode;

    public String[] getCauses() {
        return causes;
    }

    public void setCauses(String[] causes) {
        this.causes = causes;
    }

    private String[] causes;


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static EventsNativeListFragment newInstance(String zipcode, String[] causes) {
        EventsNativeListFragment fragment = new EventsNativeListFragment();
        fragment.setCauses(causes);
        fragment.setZipcode(zipcode);


        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventsNativeListFragment() {
    }

    public List EventList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchEventsTask task = new FetchEventsTask();
        task.execute(zipcode,
                     causes);
        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<AssembleEvent>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, EventList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent launchIntent = new Intent(getActivity(), EventDetailsActivity.class);
                launchIntent.putExtra("EVENT", ((AssembleEvent) EventList.get(position)).getJson().toString());
                if (launchIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(launchIntent);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "bark", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public static class AssembleEvent {
        private static final String NAME = "name";
        private static final String DESCRIPTION = "description";
        String message = "whoo hoo";
        JSONObject json = null;

        public AssembleEvent() {}

        public AssembleEvent(String message) {
            this.message = message;
        }

        public AssembleEvent(JSONObject json) {

            this.json = json;
        }

        public String toString() {
            String ret = "";

            try {
                ret += json.getString(NAME);
                ret += "\n\n\n\n";
                ret += json.get(DESCRIPTION);
            } catch (JSONException e) {
                e.printStackTrace();
                return "ERROR PROCESSING JSON";
            }

            return ret;
        }

        public URL getUrl() {
            try {
                return new URL(json.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public JSONObject getJson() {
            return json;
        }
    }

    public class FetchEventsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            if(params.length < 2) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = buildUrl((String) params[0], (String[]) params[1]); // get user location preference?

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attempting
                // to parse it.
                return null;
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

        }


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            ArrayList<AssembleEvent> events = generateEventsFromJson((String) result);
            mAdapter.clear();
            mAdapter.addAll(events);
        }

        private ArrayList<AssembleEvent> generateEventsFromJson(String result) {
            ArrayList<AssembleEvent> events = new ArrayList<AssembleEvent>();


            JSONArray eventsJson = null;
            try {
                eventsJson = new JSONArray(result);
                for(int i = 0; i < eventsJson.length(); i++) {
                    events.add(new AssembleEvent((JSONObject) eventsJson.get(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return events;
        }


        private URL buildUrl(String location, String[] causes) {
//            Uri.Builder builder = new Uri.Builder();
//            builder.scheme("http");
//            builder.authority("api.openweathermap.org");
//            builder.path("data/2.5/forecast/daily?");
//            builder.appendQueryParameter("q", location);
//            builder.appendQueryParameter("mode", "json");
//            builder.appendQueryParameter("units", "metric");
//            builder.appendQueryParameter("cnt", "7");
//
//            try {
//                return new URL(builder.build().toString());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            }
            try {
                return new URL("http://www.assembleapp.org/api/events");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
