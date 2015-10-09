package hhg0104.barcodeprj.task;

import android.os.AsyncTask;

import java.io.IOException;

import hhg0104.barcodeprj.connector.HttpBookSearchConnector;
import hhg0104.barcodeprj.connector.NaverBookAPI;
import hhg0104.barcodeprj.connector.ResponseListener;
import hhg0104.barcodeprj.model.BookInfo;

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

        BookInfo responseProp = new BookInfo();

        try {
            NaverBookAPI api = new NaverBookAPI(NaverBookAPI.DEFULAT_KEY, Long.valueOf(params[0]));
            return HttpBookSearchConnector.getBookSearchResult(api);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void onPostExecute(BookInfo bookInfo) {

        listener.postResponse(bookInfo);
    }
}
