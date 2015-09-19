package org.assembleapp.activismapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailsFragment extends Fragment {

    EventsNativeListFragment.AssembleEvent event;

    public EventDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        TextView text = (TextView) view.findViewById(R.id.placeholder);
        EventsNativeListFragment.AssembleEvent event;
        try {
             event = new EventsNativeListFragment.AssembleEvent(
                    getActivity().getIntent().getStringExtra(EventsNativeListFragment.EVENT));
        } catch (JSONException e) {
            renderError();
            return null;
        }
        text.setText(event.getName());

        return view;
    }

    private void renderError() {
        // TODO
    }
}
