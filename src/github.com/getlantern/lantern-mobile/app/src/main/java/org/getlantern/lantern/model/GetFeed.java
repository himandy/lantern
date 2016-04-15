package org.getlantern.lantern.model;

import android.os.AsyncTask;
import android.util.Log;

import org.getlantern.lantern.activity.LanternMainActivity;

import java.util.ArrayList; 
import  java.util.Locale;

import go.lantern.Lantern;

public class GetFeed extends AsyncTask<String, Void, ArrayList<String>> {
    private static final String TAG = "GetFeed";

    private LanternMainActivity activity;
    private String proxyAddr = "";
    private ArrayList<String> sources = new ArrayList<String>();

    public GetFeed(LanternMainActivity activity, String proxyAddr) {
        this.activity = activity;
        this.proxyAddr = proxyAddr;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String locale = Locale.getDefault().toString();
        Log.d(TAG, String.format("Fetching public feed: locale=%s; proxy addr=%s", locale, proxyAddr));

        Lantern.GetFeed(locale, proxyAddr, new Lantern.FeedProvider.Stub() {
            public void AddSource(String source) {
                sources.add(source);
            }
        });

        return sources;
    }

    @Override
    protected void onPostExecute(ArrayList<String> sources) {
        super.onPostExecute(sources);
        activity.setupFeed(sources);

        if (sources == null || sources.isEmpty() || Lantern.NullFeed()) {
            activity.showFeedError();
        }
    }
}   

