package org.dkfsoft;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by ap197_000 on 23.12.2015.
 */
public class KikimorWeather extends AsyncTask<Void, Void, String[]> {

    private AlarmManagerBroadcastReceiver alarm_manager;
    private final static String KIKIMOR_HOST = "http://weather.kikimor.ru/api.php";
    //                                     T    P
    private final static String[] sids = {"7", "6"};

    KikimorWeather(AlarmManagerBroadcastReceiver ares) {
        this.alarm_manager = ares;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String[] doInBackground(Void... params) {

        String[] res = new String[sids.length];
        StringBuilder sb = new StringBuilder();
        HttpClient httpclient = new DefaultHttpClient();
        try {
            for(int i = 0; i < sids.length; ++i) {
                // prepare correct Uri
                Uri uri = Uri.parse(KIKIMOR_HOST).buildUpon()
                        .appendQueryParameter("sid", sids[i])
                        .appendQueryParameter("act", "last")
                        .appendQueryParameter("method", "plain")
                        .build();
                //Log.d("MyWidget", uri.toString());
                HttpResponse response = httpclient.execute(new HttpGet(uri.toString()));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    res[i] = out.toString().trim();
                    out.close();
                } else {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }
        }
        catch(UnknownHostException e) {
            e.printStackTrace();
            Log.e("KikimorWeather", "Unable to resolve host name: " + e.toString());
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("KikimorWeather", e.toString());
            e.printStackTrace();
        }

        return res;
    }

    @Override
    protected void onPostExecute(String[] res) {
        //Log.d("MyWidget", "Response string elems: " + res.length);
        if (res.length == sids.length) {
            if (!res[0].substring(0,1).equals("-")) res[0] = "+" + res[0];
        }

        alarm_manager.refreshValues(res);
    }
};
