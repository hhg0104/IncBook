package hhg0104.barcodeprj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.listener.ISBNDialogListener;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-15.
 */
public class ISBNDialog extends Dialog implements View.OnClickListener {

    private ISBNDialogListener listener;

    public void setListener(ISBNDialogListener listener) {

        this.listener = listener;
    }

    public ISBNDialog(Context context) {
        super(context);
        setTitle("ISBN 입력");
        setContentView(R.layout.dialog_isbn_input);

        TextView isbnBtn = (TextView) findViewById(R.id.input_isbn_ok);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        isbnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();

        if (btnID == R.id.input_isbn_ok) {

            EditText isbnInput = (EditText) findViewById(R.id.input_isbn);
            String inputISBN = isbnInput.getText().toString().trim();

            if (inputISBN.isEmpty()) {
                ToastMessage.showError(getContext(), "ISBN을 입력해주십시오.", Toast.LENGTH_SHORT);
                return;
            }
            dismiss();
            listener.setSelectedValue(inputISBN);
        } else {
            dismiss();
        }
    }


}
