package hhg0104.barcodeprj.connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by HGHAN on 2015-09-12.
 */
public class NaverBookAPI implements BookSearchAPI {

    public static final String DEFULAT_CLIENT_ID = "W8fvLt8QtIY9kEXqxTL7";

    public static final String DEFULAT_CLIENT_SECRET = "R53Fmo8SnQ";

    private static final String URL = "https://openapi.naver.com/v1/search/book_adv.xml?";

    private Map<String, String> params = new HashMap<String, String>();

    private Map<String, String> headers = new HashMap<String, String>();

    public NaverBookAPI(String clientId, String clientSecret, long isbn) {

        params.put("d_isbn", String.valueOf(isbn));

        headers.put("X-Naver-Client-Id", clientId);
        headers.put("X-Naver-Client-Secret", clientSecret);
    }

    public NaverBookAPI(long isbn) {

        params.put("d_isbn", String.valueOf(isbn));

        headers.put("X-Naver-Client-Id", DEFULAT_CLIENT_ID);
        headers.put("X-Naver-Client-Secret", DEFULAT_CLIENT_SECRET);
    }

    public NaverBookAPI(String key, String title) {

        params.put("title", title);

        headers.put("X-Naver-Client-Id", DEFULAT_CLIENT_ID);
        headers.put("X-Naver-Client-Secret", DEFULAT_CLIENT_SECRET);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public java.net.URL getURL() {

        String address = URL;

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = params.get(key);
            if (value != null) {
                address += String.format("%s=%s&", key, value);
            }
        }

        try {
            return new URL(address);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getContentType() {
        return "text/xml";
    }
}
