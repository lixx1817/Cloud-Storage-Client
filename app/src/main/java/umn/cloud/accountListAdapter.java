package umn.cloud;

/**
 * Created by AngusY on 3/14/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class accountListAdapter extends ArrayAdapter<serviceAccount> {

    private List<serviceAccount> itemList;
    private Context context;

    public accountListAdapter(List<serviceAccount> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public serviceAccount getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.servicelist_item, null);
        }

        serviceAccount c = itemList.get(position);
        TextView text = (TextView) v.findViewById(R.id.service_name);
        text.setText(c.getName());

        TextView text1 = (TextView) v.findViewById(R.id.service_srvId);
        text1.setText(c.getSrvId());

        TextView text2 = (TextView) v.findViewById(R.id.service_email);
        text2.setText(c.getEmail());



        return v;

    }

    public List<serviceAccount> getItemList() {
        return itemList;
    }

    public void setItemList(List<serviceAccount> itemList) {
        this.itemList = itemList;
    }


}