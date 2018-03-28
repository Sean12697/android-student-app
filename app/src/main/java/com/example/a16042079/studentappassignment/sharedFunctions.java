package com.example.a16042079.studentappassignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by 16042079 on 27/03/2018.
 */

public class sharedFunctions {

    final public static String apiKey = "3ae2b20cca";

    public static String serverCallTest(String urlStr, String urlParms) throws IOException {

        URL url;
        url = new URL(urlStr);
        // create and configure the connection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // write/send/POST data to the connection using output stream and buffered writer
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(urlParms);

        // clear the writer
        writer.flush();
        writer.close();
        // close output stream
        os.close();
        // get the server response code to determine what to do next (i.e. success/error)
        String response = "";
        String line;
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                response += line;
            }
        }
        return response;
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
