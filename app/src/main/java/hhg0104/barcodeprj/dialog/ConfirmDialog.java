package hhg0104.barcodeprj.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by hhg0104 on 2018-03-09.
 */
public class ConfirmDialog {

    private Context context = null;

    public ConfirmDialog(Context context){
        this.context = context;
    }

    /**
     * Confirm Dialog를 표출한다.
     * @param message 질문 메세지
     * @param action 이벤트 콜백
     */
    public void showConfirmDialog(String message, DialogInterface.OnClickListener action) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);
        builder.setPositiveButton("Yes", action);
        builder.setNegativeButton("No", action);

        builder.show();
    }
}
