package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.connector.ServerConnector;
import hhg0104.barcodeprj.listener.HttpListener;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.model.ResponseModel;
import hhg0104.barcodeprj.utils.Action;
import hhg0104.barcodeprj.utils.HttpAction;
import hhg0104.barcodeprj.utils.InputMode;
import hhg0104.barcodeprj.utils.IntentExtraEntry;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-25.
 */
public class DeleteBookClickListener implements DialogInterface.OnClickListener{

    private Activity activity = null;

    private String bookId = null;

    public DeleteBookClickListener(Activity activity, String bookId) {
        this.activity = activity;
        this.bookId = bookId;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked

                final Context context = activity.getApplicationContext();

                ServerConnector conn = new ServerConnector(new HttpListener() {
                    @Override
                    public void getResult(ResponseModel response) {

                        if (response.getStatusCode() != 200) {
                            ToastMessage.showError(context, response.getData(), Toast.LENGTH_LONG);
                            return;
                        }

                        ToastMessage.showDefault(context, "Deleted", Toast.LENGTH_SHORT);

                        //activity.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                        Intent mainIntent = new Intent(activity, MainActivity.class);
                        mainIntent.putExtra(IntentExtraEntry.ACTION, Action.REFRESH);

                        activity.startActivity(mainIntent);
                        activity.finish();
                    }
                }, context);

                conn.setHttpInfo(bookId, null, null);
                conn.execute(HttpAction.DELETE_BOOK);

                break;

            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }
}
