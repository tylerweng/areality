package com.example.areality;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Terry on 4/9/17.
 */

public class PostRequest {
    private static final String TAG = "PostRequest";
    private String requestUrl;
    private String requestParams;

    public PostRequest(String url, String params) {
        requestUrl = url;
        requestParams = params;
    }

    public String execute() throws Exception {
        PostTask pu = new PostTask();
        return pu.execute(requestParams).get();
    }

    private class PostTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL areality = new URL(requestUrl);
                HttpsURLConnection con = (HttpsURLConnection) areality.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setDoOutput(true);

                con.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(params[0]);
                dos.flush();
                dos.close();

                int responseCode = con.getResponseCode();
                Log.d(TAG, "\nSending 'POST' request to URL: " + areality);
                Log.d(TAG, "Post parameters: " + params[0]);
                Log.d(TAG, "Response code: " + responseCode);

                BufferedReader in;

                if (responseCode == 401) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } catch (Exception e) {
                return "{ \"error\": \"could not complete request\" }";
            }
        }
    }
}
