package org.assembleapp.activismapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailsFragment extends Fragment {

    public EventDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        TextView text = (TextView) view.findViewById(R.id.placeholder);
        text.setText(getActivity().getIntent().getStringExtra(EventsNativeListFragment.EVENT));

        return view;
    }
}
