package umn.cloud;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Created by AngusY on 3/14/15.
 * Asyntask for fetching token and send Json to server
 */
public class GetUsernameTask extends AsyncTask<Void, Void, Void> {
    addServiceAccount mActivity;
    String mScope;
    String mEmail;

    public static final String TAG = "YourClassName";
    public static final String wurl = "ec2-54-213-7-206.us-west-2.compute.amazonaws.com";


    GetUsernameTask(addServiceAccount activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            String token = fetchToken();
            if (token != null) {
                // Insert the good stuff here.
                // Use the token to access the user's Google data.
                createJson(token);
                Log.d("This is the output", token);

            }
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.

        }
        return null;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException e) {
            Intent intent = (e).getIntent();
            mActivity.handleException(e);  //User permission needed

        } catch (GoogleAuthException fatalException) {
            Log.d("An exception has occured", fatalException.toString());
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.

        }
        return null;
    }

    protected void createJson(String code) throws IOException{
        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("AcessCode", code);
            jsonobj.put("name", "taiqiang");
            jsonobj.put("userID", "taiqiang123");
            sendJson(jsonobj);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    protected void sendJson(JSONObject jsonobj)throws IOException{
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(wurl);
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
        }
        return total.toString();
    }

}