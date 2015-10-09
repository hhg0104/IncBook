package hhg0104.barcodeprj.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * Created by HGHAN on 2015-09-17.
 */
public class ToastMessage {

    public static void showDefault(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showError(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
        view.setTextColor(Color.RED);
        toast.show();
    }
}
