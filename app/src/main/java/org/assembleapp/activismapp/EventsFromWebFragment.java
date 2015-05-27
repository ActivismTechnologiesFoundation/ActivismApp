package org.assembleapp.activismapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.assembleapp.activismapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFromWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFromWebFragment extends Fragment {
    private String zipcode;
    private String causes;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param zipcode Parameter 1.
     * @param causes Parameter 2.
     * @return A new instance of fragment EventsFromWebFragment.
     */
    public static EventsFromWebFragment newInstance(String zipcode, String causes) {
        EventsFromWebFragment fragment = new EventsFromWebFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.ZIPCODE, zipcode);
        args.putString(MainActivity.CAUSES, causes);
        fragment.setArguments(args);
        return fragment;
    }

    public EventsFromWebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events_from_web, container, false);


        WebView webView = (WebView) rootView.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // TODO include zip/causes
        webView.loadUrl("http://www.assembleapp.org/events");
        return rootView;
    }


}
