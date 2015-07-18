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
import android.widget.ListAdapter;
import android.widget.TextView;


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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        String[] availableCauseKeys = getResources().getStringArray(R.array.cause_values);
        String[] availableCauseLabels = getResources().getStringArray(R.array.cause_entries);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> selectedCausesSet = sharedPref.getStringSet(getString(R.string.cause_list_key), Collections.<String>emptySet());
        List<String> selectedCauses = Arrays.asList(selectedCausesSet.toArray(new String[0]));

        mAdapter = new CauseEntryAdapter(getActivity(), Arrays.asList(availableCauseLabels), Arrays.asList(availableCauseKeys), selectedCauses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cause, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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


    private class CauseEntryAdapter extends ArrayAdapter {
        List<String> selectedCauseKeys;
        List<String> availableCauseKeys;
        List<String> selectedCauseLabels;

        public CauseEntryAdapter(Context context, List<String> causeLabels, List<String> availableCauseKeys, List<String> selectedCauseKeys) {
            super(context, 0, causeLabels);
            if (selectedCauseKeys != null)
                this.selectedCauseKeys = selectedCauseKeys;
            else
                this.selectedCauseKeys = new LinkedList<>();

            this.availableCauseKeys = availableCauseKeys;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            String cause = (String) getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cause_list_item, parent, false);

            }
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.cause_checkbox);
            checkbox.setText(cause);
            checkbox.setChecked(selectedCauseKeys.contains(availableCauseKeys.get(position)));

            return convertView;
        }
    }
}
