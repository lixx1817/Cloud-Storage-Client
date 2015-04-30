package umn.cloud;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;


public class addServiceAccount extends ActionBarActivity {

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;
    static final int CODE_SELECT_TARGET_ACCOUNT=2000;
    static final String googleDrive_Scope="https://www.googleapis.com/auth/drive.file ";
    static final String  googlePlus_Scope="https://www.googleapis.com/auth/plus.login";
     static final String SCOPE =
            "oauth2:server:client_id:979484502896-a7pjq3r1ksmprminduh5b31t445l6n86.apps.googleusercontent.com:api_scope:"+googleDrive_Scope+googlePlus_Scope;
    static final String TAG="test";

    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    ArrayList<serviceAccount> targetAccountList=new ArrayList<serviceAccount>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_add_service_account);
        EditText txtName = (EditText)findViewById(R.id.inputAccountName_new);
        txtName.addTextChangedListener(watch);
        String nameString = txtName.getText().toString();
        Button addAccount= (Button) findViewById(R.id.btnAddAccount);
        addAccount.setEnabled(false);
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

    TextWatcher watch = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable arg0) {

        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub
            Button addAccount= (Button) findViewById(R.id.btnAddAccount);
            if(s.length()==0) addAccount.setEnabled(false);
            else addAccount.setEnabled(true);
        }};

    public void greetUser(View view){
        pickUserAccount();
    }


    public void startSelection(View view){
        Intent intent = new Intent(this, selectTargetAccount.class);
        startActivityForResult(intent,CODE_SELECT_TARGET_ACCOUNT);
    }

    public void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
       // Toast.makeText(this, "You have added your account successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                EditText txtName = (EditText)findViewById(R.id.inputAccountName_new);
                String nameString = txtName.getText().toString();
                new GetTokenTask(addServiceAccount.this, mEmail, SCOPE, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR,nameString,targetAccountList).execute();


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
            handleAuthorizeResult(resultCode, data);
            return;
        }
        else if(requestCode==CODE_SELECT_TARGET_ACCOUNT){

            // TODO Define activity for selecting account
            targetAccountList=data.getParcelableArrayListExtra("user.selection");
            //Bundle userSelection = this.getIntent().getBundleExtra("search.resultSet");
            if(targetAccountList!=null) {
                for(serviceAccount t: targetAccountList){
                    Log.d("debug output for receiver", t.toString());
                }
            }
            else Log.d("None!",TAG);


        }

        // Later, more code will go here to handle the result from some exceptions...
    }
    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            Toast.makeText(this, "Unknown error encountered", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            EditText txtName = (EditText)findViewById(R.id.inputAccountName_new);
            String nameString = txtName.getText().toString();
            new GetTokenTask(addServiceAccount.this, mEmail, SCOPE, REQUEST_CODE_PICK_ACCOUNT,nameString,targetAccountList).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "user has rejected this authorization", Toast.LENGTH_SHORT).show();
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

    /** Checks whether the device currently has a network connection */
    protected boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
