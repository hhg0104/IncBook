package hhg0104.barcodeprj.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import hhg0104.barcodeprj.connector.HttpBookSearchConnector;
import hhg0104.barcodeprj.connector.NaverBookAPI;
import hhg0104.barcodeprj.connector.ResponseListener;
import hhg0104.barcodeprj.model.BookInfo;
import hhg0104.barcodeprj.utils.ToastMessage;

/**
 * Created by HGHAN on 2015-09-22.
 */
public class IsbnReadTask extends AsyncTask<Long, Void, BookInfo> {

    private ResponseListener listener;

    public IsbnReadTask(ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected BookInfo doInBackground(Long... params) {

        try {
            NaverBookAPI api = new NaverBookAPI(Long.valueOf(params[0]));
            return HttpBookSearchConnector.getBookSearchResult(api);

        } catch (Exception e) {

            BookInfo errorBook = new BookInfo();
            errorBook.setError(e);

            return errorBook;
        }
    }

    @Override
    public void onPostExecute(BookInfo bookInfo) {
        listener.postResponse(bookInfo);
    }
}
