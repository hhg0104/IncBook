package hhg0104.barcodeprj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.listener.OptionDialogListener;
import hhg0104.barcodeprj.utils.RegisterOption;

/**
 * Created by HGHAN on 2015-09-15.
 */
public class OptionDialog extends Dialog implements View.OnClickListener {

    private OptionDialogListener listener;

    public void setListener(OptionDialogListener listener){
        this.listener = listener;
    }

    public OptionDialog(Context context) {
        super(context);
        setTitle("Select");
        setContentView(R.layout.dialog_options);

        TextView barcodeBtn = (TextView) findViewById(R.id.option_barcode);
        barcodeBtn.setOnClickListener(this);

        TextView isbnBtn = (TextView) findViewById(R.id.option_isbn);
        isbnBtn.setOnClickListener(this);

        TextView selfBtn = (TextView) findViewById(R.id.option_self);
        selfBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();

        if (btnID == R.id.option_barcode) {
            dismiss();
            listener.setSelectedValue(RegisterOption.BARCODE);
        } else if (btnID == R.id.option_isbn) {
            dismiss();
            listener.setSelectedValue(RegisterOption.ISBN);

        } else if (btnID == R.id.option_self) {
            dismiss();
            listener.setSelectedValue(RegisterOption.SELF);

        } else {
            dismiss();
            listener.setSelectedValue(RegisterOption.NONE);
        }
    }
}
