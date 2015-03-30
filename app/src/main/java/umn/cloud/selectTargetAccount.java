package umn.cloud;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class selectTargetAccount extends ListActivity {

    public accountListAdapter adpt;
    public static final String list_url = "http://ec2-54-213-7-206.us-west-2.compute.amazonaws.com:8080/list_srvacc";
//    public final ProgressDialog dialog = new ProgressDialog(selectTargetAccount.this);
    List<serviceAccount> userChoices= new ArrayList<serviceAccount>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_target_account);

        adpt  = new accountListAdapter(new ArrayList<serviceAccount>(), this);
        ListView lView = (ListView) findViewById(android.R.id.list);

        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lView.setItemsCanFocus(false);
        lView.setAdapter(adpt);

        // Exec async load task
        (new getSAccountTask(selectTargetAccount.this)).execute(list_url);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        if (v != null) {
            CheckedTextView checkBox = (CheckedTextView)v.findViewById(R.id.service_name);
            checkBox.toString();
            if(checkBox.isChecked()) {
                userChoices.add(adpt.getItem(position));
            }
        }
        //serviceAccount selectedValue =  adpt.getItem(position);
        //userChoices.add(selectedValue);

        //Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }

}
