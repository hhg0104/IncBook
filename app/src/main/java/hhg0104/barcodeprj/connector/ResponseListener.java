package hhg0104.barcodeprj.connector;

import hhg0104.barcodeprj.model.BookInfo;

/**
 * Created by HGHAN on 2015-09-22.
 */
public interface ResponseListener {

    public void postResponse(BookInfo resultProp);
}
