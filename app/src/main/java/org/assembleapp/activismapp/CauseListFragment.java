package org.assembleapp.activismapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.assembleapp.activismapp.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class CauseListFragment extends Fragment implements AbsListView.OnItemClickListener {

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static CauseListFragment newInstance(String param1, String param2) {
        CauseListFragment fragment = new CauseListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CauseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CauseEntryAdapter(getActivity(), getCauseListFromPreferences());
    }

    public List<String> getCheckedCauses() {
        List<String> ret = new LinkedList<String>();

        for(int i = 0; i < mAdapter.getCount(); i++) {
            CauseListEntry cause = (CauseListEntry) mAdapter.getItem(i);
            if(cause.isChecked()) {
                ret.add(cause.getKey());
            }
        }
        return ret;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cause, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CauseListEntry cause = (CauseListEntry) mAdapter.getItem(position);
                cause.setChecked(!cause.isChecked());
            }

        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity().getApplicationContext(), "foo", Toast.LENGTH_SHORT);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private List<CauseListEntry> getCauseListFromPreferences() {
        List<CauseListEntry> ret = new LinkedList<>();

        String[] availableCauseKeys = getResources().getStringArray(R.array.cause_values);
        String[] availableCauseLabels = getResources().getStringArray(R.array.cause_entries);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> selectedCausesSet = sharedPref.getStringSet(getString(R.string.cause_list_key), Collections.<String>emptySet());
        List<String> selectedCauses = Arrays.asList(selectedCausesSet.toArray(new String[0]));

        for(int i = 0; i < availableCauseLabels.length; i++) {
            ret.add(new CauseListEntry(availableCauseKeys[i], availableCauseLabels[i], selectedCausesSet.contains(availableCauseKeys[i])));
        }


        return ret;
    }
    private class CauseListEntry {
        protected String key;
        protected String label;
        protected boolean checked;

        public CauseListEntry(String key, String label, boolean checked) {
            this.key = key;
            this.label = label;
            this.checked = checked;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }


    private class CauseEntryAdapter extends ArrayAdapter {
        public CauseEntryAdapter(Context context, List<CauseListEntry> causes) {
            super(context, 0, causes);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final CauseListEntry cause = (CauseListEntry) getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cause_list_item, parent, false);
            }
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.cause_checkbox);
            checkbox.setText(cause.getLabel());
            checkbox.setChecked(cause.isChecked());

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        cause.setChecked(true);
                    }
                    else {
                        cause.setChecked(false);
                    }

                }
            });

            return convertView;
        }
    }
}
