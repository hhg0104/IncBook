package hhg0104.barcodeprj.connector;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import hhg0104.barcodeprj.model.BookInfo;

/**
 * Created by HGHAN on 2015-09-12.
 */
public class HttpBookSearchConnector {

    public static BookInfo getBookSearchResult(BookSearchAPI api) throws IOException {

        String address = api.getURL();
        Map<String, String> params = api.getParams();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = params.get(key);
            if (value != null) {
                address += String.format("%s=%s&", key, value);
            }
        }

        if (api instanceof NaverBookAPI) {
            return getPropertiesFromXML(address);
        } else {
            return null;
        }

    }

    private static BookInfo getPropertiesFromXML(String address) {

        BookInfo bookInfo = new BookInfo();

        try {
            URL url = new URL(address); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();
            StringBuffer buffer = new StringBuffer();

            boolean itemStart = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    String name = xpp.getName();

                    if (itemStart == false && "item".equalsIgnoreCase(name)) {
                        itemStart = true;
                        eventType = xpp.next();
                        continue;
                    }

                    if (itemStart == false) {
                        eventType = xpp.next();
                        continue;
                    }

                    if ("title".equalsIgnoreCase(name)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            bookInfo.setBookTitle(xpp.getText().trim());
                        }
                    }

                    if ("image".equalsIgnoreCase(name)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            bookInfo.setImagePath(xpp.getText().trim());
                        }
                    }

                    if ("author".equalsIgnoreCase(name)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            bookInfo.setAuthor(xpp.getText().trim());
                        }
                    }

                    if ("publisher".equalsIgnoreCase(name)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            bookInfo.setPublisher(xpp.getText().trim());
                        }
                    }

                    if ("description".equalsIgnoreCase(name)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            bookInfo.setDescription(xpp.getText().trim());
                        }

                        break;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String name = xpp.getName();

                    if ("item".equalsIgnoreCase(name)) {
                        break;
                    }
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return bookInfo;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }

//    public static String getResult(String url){
//        // Create an instance of HttpClient.
//        HttpClient client = new HttpClient();
//
//        // Create a method instance.
//        GetMethod method = new GetMethod(url);
//
//        // Provide custom retry handler is necessary
//        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//                new DefaultHttpMethodRetryHandler(3, false));
//
//        String result = StringConstants.EMPTY;
//        try {
//            // Execute the method.
//            int statusCode = client.executeMethod(method);
//
//            if (statusCode != HttpStatus.SC_OK) {
//                System.err.println("Method failed: " + method.getStatusLine());
//            }
//
//            // Read the response body.
//            byte[] responseBody = method.getResponseBody();
//
//            // Deal with the response.
//            // Use caution: ensure correct character encoding and is not binary data
//            result = new String(responseBody);
//        } catch (HttpException e) {
//            System.err.println("Fatal protocol violation: " + e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("Fatal transport error: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            // Release the connection.
//            method.releaseConnection();
//        }
//
//        return result;
//    }

//    public static String getResult(String uri, HttpMethod httpMethod) throws IOException {
//
//        HttpUriRequest request = getHttpRequest(uri, httpMethod);
//        if (request == null) {
//            throw new IOException("유효하지 않은 HttpMethod를 입력하였습니다.");
//        }
//
//        HttpClient httpClient = new DefaultHttpClient();
//        BasicHttpContext context = new BasicHttpContext();
//        HttpResponse response = httpClient.execute(request, context);
//
//        HttpEntity entity = response.getEntity();
//        try {
//
//            if (entity != null) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
//
//                StringBuffer strBuf = new StringBuffer();
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    strBuf.append(line);
//                }
//
//                return strBuf.toString();
//            } else {
//                return "";
//            }
//        } finally {
//            request.abort();
//            httpClient.getConnectionManager().shutdown();
//        }
//
//    }
//
//    private static HttpUriRequest getHttpRequest(String uri, HttpMethod httpMethod) {
//
//        switch (httpMethod) {
//            case GET:
//                return new HttpGet(uri);
//            case POST:
//                return new HttpPost(uri);
//            case PUT:
//                return new HttpPut(uri);
//            case DELETE:
//                return new HttpDelete(uri);
//        }
//
//        return null;
//    }
}
