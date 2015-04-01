package umn.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class editServiceAccount extends ActionBarActivity {

    accountListAdapter adpt=null;
    public static final String list_url = "http://ec2-54-213-7-206.us-west-2.compute.amazonaws.com:8080/list_srvacc";
    //    public final ProgressDialog dialog = new ProgressDialog(selectTargetAccount.this);
    ArrayList<serviceAccount> userChoices= new ArrayList<serviceAccount>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_account);
        adpt  = new accountListAdapter(new ArrayList<serviceAccount>(), this);
        ListView lView = (ListView) findViewById(android.R.id.list);

        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lView.setItemsCanFocus(false);
        lView.setAdapter(adpt);

        // Exec async load task
        (new getSAccountTask(editServiceAccount.this)).execute(list_url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_service_account, menu);
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

    public void startSelection(View view){
        Intent intent = new Intent(this, selectTargetAccount.class);
        startActivity(intent);
    }
}
