package hhg0104.barcodeprj.model;

/**
 * Created by HGHAN on 2015-09-28.
 */
public class ResponseModel {

    private int statusCode;

    private String data;

    public ResponseModel(int statusCode, String data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getData() {
        return data;
    }
}
