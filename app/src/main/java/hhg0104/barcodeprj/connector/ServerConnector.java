package hhg0104.barcodeprj.connector;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import hhg0104.barcodeprj.listener.HttpListener;
import hhg0104.barcodeprj.model.ResponseModel;
import hhg0104.barcodeprj.utils.HttpAction;
import hhg0104.barcodeprj.utils.ServerConfiguration;
import hhg0104.barcodeprj.utils.StringConstants;

/**
 * Created by HGHAN on 2015-09-28.
 */
public class ServerConnector extends AsyncTask<String, Void, ResponseModel> {

    private static final String SCHEME = "http://";

    private HttpListener listener;

    private String requestJson;

    private String bookID;

    private Map<String, String> params = new HashMap<String, String>();

    ProgressDialog progress;

    public void setHttpInfo(String bookID, String requestJson, Map<String, String> params) {
        this.requestJson = requestJson;
        this.bookID = bookID;

        if (params != null) {
            this.params.putAll(params);
        }
    }

    public ServerConnector(HttpListener listener, Context context) {

        this.listener = listener;

        progress = new ProgressDialog(context);
    }

    private String getBooks() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        String result = HttpConnector.get(SCHEME + host + StringConstants.COLON + port + "/books");

        return result;
    }

    private String getSearchBooks() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        String keyword = params.get("keyword");

        String result = HttpConnector.get(SCHEME + host + StringConstants.COLON + port + "/books/search?keyword=" + keyword);

        return result;
    }

    private String getBook() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        String result = HttpConnector.get(SCHEME + host + StringConstants.COLON + port + "/books/" + bookID);

        return result;
    }

    private String addBook() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        return HttpConnector.post(SCHEME + host + StringConstants.COLON + port + "/book", requestJson);
    }

    private String editBook() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        return HttpConnector.put(SCHEME + host + StringConstants.COLON + port + "/books/" + bookID, requestJson);
    }

    private String deleteBook() throws InvalidResponseException {

        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String host = serverConfig.getHost();
        String port = String.valueOf(serverConfig.getPort());

        String result = HttpConnector.delete(SCHEME + host + StringConstants.COLON + port + "/books/" + bookID);

        return result;
    }

    @Override
    protected void onPreExecute() {
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setMessage("로딩중입니다...");
//
//        progress.show();
        super.onPreExecute();
    }

    @Override
    protected ResponseModel doInBackground(String... params) {

        String action = params[0];
        String requestJson = null;
        if (params.length > 1) {
            requestJson = params[1];
        }

        try {
            if (HttpAction.GET_BOOKS.equals(action)) {
                return new ResponseModel(200, getBooks());

            } else if (HttpAction.GET_BOOK.equals(action)) {
                return new ResponseModel(200, getBook());

            } else if (HttpAction.ADD_BOOK.equals(action)) {
                return new ResponseModel(200, addBook());

            } else if (HttpAction.EDIT_BOOK.equals(action)) {
                return new ResponseModel(200, editBook());

            } else if (HttpAction.DELETE_BOOK.equals(action)) {
                return new ResponseModel(200, deleteBook());

            } else if (HttpAction.SEARCH_BOOKS.equals(action)) {
                return new ResponseModel(200, getSearchBooks());

            } else {
                return new ResponseModel(404, "알수없는 요청입니다.");
            }
        } catch (InvalidResponseException e) {
            return new ResponseModel(e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(ResponseModel response) {
//        progress.dismiss();

        int statusCode = response.getStatusCode();
        String message = response.getData();
        if(statusCode != 200 && message.contains("ETIMEDOUT")){
            response = new ResponseModel(404, "서버를 찾지 못했습니다. 설정을 확인하세요.");
        }

        listener.getResult(response);
    }
}
