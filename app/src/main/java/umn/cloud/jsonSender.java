package umn.cloud;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Created by AngusY on 3/19/15.
 */
public class jsonSender {



    public void sendJsonObject(JSONObject jsonobj, String url)throws IOException {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(url);
            StringEntity se = new StringEntity(jsonobj.toString());
            //se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httppostreq.setEntity(se);
            HttpResponse httpresponse = httpclient.execute(httppostreq);
            HttpEntity resultentity = httpresponse.getEntity();
            if(resultentity != null) {
                InputStream inputstream = resultentity.getContent();
                Header contentencoding = httpresponse.getFirstHeader("Content-Encoding");
                if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")) {
                    inputstream = new GZIPInputStream(inputstream);
                }

                String resultstring = convertStreamToString(inputstream);
                inputstream.close();
                resultstring = resultstring.substring(1,resultstring.length()-1);
                //recvdref.setText(resultstring + "\n\n" + httppostreq.toString().getBytes());
//        		JSONObject recvdjson = new JSONObject(resultstring);
//            	recvdref.setText(recvdjson.toString(2));
            }
        } catch (Exception e) {
            //recvdref.setText("Error Occurred while processing JSON");
            //recvdref.setText(e.getMessage());
            Log.d("exception sending Json has occured", e.toString());
        }
    }
    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            //Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
            Log.d("exception has occured", e.toString());
        }
        return total.toString();
    }
}
