package hhg0104.barcodeprj.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import hhg0104.barcodeprj.listener.CoverTaskListener;
import hhg0104.barcodeprj.utils.ServerConfiguration;

/**
 * Created by HGHAN on 2015-09-25.
 */
public class CoverTask extends AsyncTask<SharedPreferences, Void, Boolean> {

    private CoverTaskListener listener;

    public CoverTask(CoverTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(SharedPreferences... params) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        SharedPreferences preferences = (SharedPreferences) params[0];

        return ServerConfiguration.hasServerConfiguration(preferences);
    }

    @Override
    protected void onPostExecute(Boolean hasInfo) {
        listener.hasServerInfo(hasInfo);
    }
}
