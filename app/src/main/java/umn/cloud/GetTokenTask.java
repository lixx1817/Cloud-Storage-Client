package umn.cloud;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by AngusY on 3/14/15.
 * Asyntask for fetching token and send Json to server
 */
public class GetTokenTask extends AsyncTask<Void, Void, Void> {
    addServiceAccount mActivity;
    String mScope;
    String mEmail;
    String name;
    int status;


    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;
    public static final String TAG = "YourClassName";
    public static final String register_url = "http://ec2-54-213-7-206.us-west-2.compute.amazonaws.com:8080/new_acc";


    GetTokenTask(addServiceAccount activity, String name, String scope, int status,String inputName) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
        this.status=status;
        this.name=inputName;

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
                String check= createJson(token);
                Log.d("This is the output", token);
                if(!check.equals("Success")){ Toast.makeText(mActivity, R.string.add_fail, Toast.LENGTH_SHORT).show();}

            }
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.

        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        if(status==REQUEST_CODE_PICK_ACCOUNT) Toast.makeText(mActivity, R.string.add_success, Toast.LENGTH_SHORT).show();

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

    protected String createJson(String code) throws IOException{
        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        jsonSender jsender=new jsonSender();
        String result="";
        try {
            // adding some keys
            jsonobj.put("AcessCode", code);
            jsonobj.put("name", name);
            result=jsender.sendJsonObject(jsonobj,register_url,"statusMsg");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}