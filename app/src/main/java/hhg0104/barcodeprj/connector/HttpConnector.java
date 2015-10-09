package hhg0104.barcodeprj.connector;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HGHAN on 2015-09-28.
 */
public class HttpConnector {

    public String post(String address, String requestJson) throws InvalidResponseException {
        return send(address, requestJson, HttpMethod.POST);
    }

    public String get(String address) throws InvalidResponseException {
        return send(address, null, HttpMethod.GET);
    }

    public String delete(String address) throws InvalidResponseException {
        return send(address, null, HttpMethod.DELETE);
    }

    public String put(String address, String requestJson) throws InvalidResponseException {
        return send(address, requestJson, HttpMethod.PUT);
    }


    private String send(String address, String requestJson, HttpMethod method) throws InvalidResponseException {

        HttpURLConnection conn = null;
        InputStream in = null;

        try {
            URL url = new URL(address);

            conn = (HttpURLConnection) url.openConnection();

            if (conn == null) {

            }
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(method.toString());

            if (requestJson != null) {
                conn.setRequestProperty("Content-Type", "application/json");
                byte[] outputInBytes = requestJson.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }

            if (conn.getResponseCode() != 200) {
                throw new InvalidResponseException("서버를 확인해주십시오.", conn.getResponseCode());
            }
            in = new BufferedInputStream(conn.getInputStream());

            return readResponse(in);

        } catch (IOException e) {
            throw new InvalidResponseException("서버를 확인해주십시오.", 500);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new InvalidResponseException(e.getMessage(), 500);
                }
            }
        }
    }


    private String readResponse(InputStream is) throws InvalidResponseException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toString();
        } catch (IOException e) {
            throw new InvalidResponseException(e.getMessage(), 500);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    throw new InvalidResponseException(e.getMessage(), 500);
                }
            }
        }

    }
}
