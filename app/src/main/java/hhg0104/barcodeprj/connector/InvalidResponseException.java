package hhg0104.barcodeprj.connector;

/**
 * Created by HGHAN on 2015-09-28.
 */
public class InvalidResponseException extends Throwable {

    private final int statusCode;

    public InvalidResponseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode(){
        return statusCode;
    }
}
