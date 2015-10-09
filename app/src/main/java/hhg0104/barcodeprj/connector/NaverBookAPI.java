package hhg0104.barcodeprj.connector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HGHAN on 2015-09-12.
 */
public class NaverBookAPI implements BookSearchAPI {

    public static final String DEFULAT_KEY = "3eca9ea52261d35cf9c48fc3c99478b2";

    private static final String URL = "http://openapi.naver.com/search?";

    private Map<String, String> params = new HashMap<String, String>();

    public NaverBookAPI(String key, long isbn) {
        setDefaultParams();
        params.put("key", key);
        params.put("d_isbn", String.valueOf(isbn));
    }

    private void setDefaultParams() {
        params.put("query", "art");
        params.put("target", "book_adv");
    }

    public NaverBookAPI(int isbn) {
        setDefaultParams();
        params.put("key", DEFULAT_KEY);
        params.put("d_isbn", String.valueOf(isbn));
    }

    public NaverBookAPI(String key, String title) {
        setDefaultParams();
        params.put("key", key);
        params.put("title", title);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public String getContentType() {
        return "text/xml";
    }
}
