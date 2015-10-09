package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.listener.CoverTaskListener;
import hhg0104.barcodeprj.task.CoverTask;
import hhg0104.barcodeprj.utils.ServerConfiguration;

/**
 * Created by HGHAN on 2015-09-25.
 */
public class CoverActivity extends Activity {

    private CoverActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);

        thisActivity = this;

        CoverTask task = new CoverTask(new CoverTaskListener() {
            @Override
            public void hasServerInfo(boolean hasInfo) {
                Intent nextIntent = null;
                if (hasInfo == false) {
                    nextIntent = new Intent(thisActivity, ServerConfigActivity.class);
                }else{
                    nextIntent = new Intent(thisActivity, MainActivity.class);
                }

                startActivity(nextIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        task.execute(getSharedPreferences(ServerConfiguration.SERVER_PREFERENCE, MODE_PRIVATE));
    }
}
