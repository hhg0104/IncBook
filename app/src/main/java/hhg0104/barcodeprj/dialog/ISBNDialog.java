package hhg0104.barcodeprj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.listener.ISBNDialogListener;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-15.
 */
public class ISBNDialog extends Dialog implements View.OnClickListener {

    private ISBNDialogListener listener;

    private List<BookInfo> books = new ArrayList<BookInfo>();

    public ISBNDialog(Context context, List<BookInfo> books) {
        super(context);
        this.books = books;

        setTitle("ISBN");
        setContentView(R.layout.dialog_isbn_input);

        TextView isbnBtn = (TextView) findViewById(R.id.input_isbn_ok);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        isbnBtn.setOnClickListener(this);
    }

    public void setListener(ISBNDialogListener listener) {

        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();

        if (btnID == R.id.input_isbn_ok) {

            EditText isbnInput = (EditText) findViewById(R.id.input_isbn);
            String inputISBN = isbnInput.getText().toString().trim();

            if (inputISBN.isEmpty()) {
                ToastMessage.showError(getContext(), "Input 13 digit ISBN.", Toast.LENGTH_SHORT);
                return;
            }

            if(inputISBN.length() != 13) {
                ToastMessage.showError(getContext(), "Input 13 digit ISBN.", Toast.LENGTH_SHORT);
                return;
            }

            boolean exist = false;
            for (BookInfo book : books) {
                String isbn = book.getIsbn();
                if(inputISBN.equals(isbn)) {
                    exist = true;
                    break;
                }
            }

            if(exist) {
                ToastMessage.showError(getContext(), "Already the book info that has same ISBN exists", Toast.LENGTH_SHORT);
                return;
            }

            dismiss();
            listener.setSelectedValue(inputISBN);
        } else {
            dismiss();
        }
    }


}
