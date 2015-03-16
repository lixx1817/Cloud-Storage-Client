package umn.cloud;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class addServiceAccount extends ActionBarActivity {

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;
    static final String googleDrive_Scope="https://www.googleapis.com/auth/drive.file ";
    static final String  googlePlus_Scope="https://www.googleapis.com/auth/plus.login";
     static final String SCOPE =
            "oauth2:server:client_id:979484502896-bu5qe6a14sgptmnamihtof8skbfgfbe5.apps.googleusercontent.com:api_scope:"+googleDrive_Scope+googlePlus_Scope;
    static final String TAG="test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_add_service_account);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_service_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickUserAccount(View view) {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }
    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Toast.makeText(this, mEmail, Toast.LENGTH_SHORT).show();
                // With the account name acquired, go get the auth token
                //getUsername();
                new GetUsernameTask(addServiceAccount.this, mEmail, SCOPE).execute();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
            }
        }
            else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                    requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                    && resultCode == RESULT_OK) {
                // Receiving a result that follows a GoogleAuthException, try auth again
                Log.d("we have returned", TAG);
                handleAuthorizeResult(resultCode, data);
                return;
            }

        // Later, more code will go here to handle the result from some exceptions...
    }
    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            //show("Unknown error, click the button again");
            return;
        }
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Retrying");
            new GetUsernameTask(addServiceAccount.this, mEmail, SCOPE).execute();
            //getTask(this, mEmail, SCOPE).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            //show("User rejected authorization.");
            return;
        }
        //show("Unknown error, click the button again");
    }

    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            addServiceAccount.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

}
