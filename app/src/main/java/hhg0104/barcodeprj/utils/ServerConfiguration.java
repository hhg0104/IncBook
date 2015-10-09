package hhg0104.barcodeprj.utils;

import android.content.SharedPreferences;

/**
 * Created by HGHAN on 2015-09-24.
 */
public class ServerConfiguration {

    public static final String SERVER_HOST = "server_host";

    public static final String SERVER_PORT = "server_port";

    public static final String SERVER_PREFERENCE = "server_preference";

    private String host = StringConstants.EMPTY;

    private int port = 0;

    private static ServerConfiguration instance;

    public static ServerConfiguration getInstance() {
        if (instance == null) {
            instance = new ServerConfiguration();
        }

        return instance;
    }

    public void init(String host, int port) {
        if (host != null) {
            this.host = host;
        }

        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public static boolean hasServerConfiguration(SharedPreferences serverPrefer) {

        String host = serverPrefer.getString(ServerConfiguration.SERVER_HOST, null);
        int port = serverPrefer.getInt(ServerConfiguration.SERVER_PORT, 0);

        return hasInfo(host, port);
    }

    private static boolean hasInfo(String host, int port) {
        if (host == null || port == 0) {
            return false;
        }
        return true;
    }

    public static void setServerPreferences(SharedPreferences preference, String host, int port) {

        SharedPreferences.Editor serverEditor = preference.edit();
        serverEditor.putString(ServerConfiguration.SERVER_HOST, host);
        serverEditor.putInt(ServerConfiguration.SERVER_PORT, port);

        serverEditor.commit();

        ServerConfiguration.getInstance().init(host, port);
    }
}
