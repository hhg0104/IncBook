package hhg0104.barcodeprj.connector;

import com.google.zxing.client.android.HttpHelper;

import java.net.URL;
import java.util.Map;

import hhg0104.barcodeprj.utils.StringConstants;

/**
 * Created by HGHAN on 2015-09-12.
 */
public interface BookSearchAPI {

    public Map<String, String> getParams();

    public Map<String, String> getHeaders();

    public URL getURL();

    public String getContentType();
}
