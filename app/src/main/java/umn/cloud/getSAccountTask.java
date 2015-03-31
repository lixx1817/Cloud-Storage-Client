package umn.cloud;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AngusY on 3/28/15.
 * Retrieve a list of account from server and render it using listview
 */
public class getSAccountTask extends AsyncTask<String, Void, List<serviceAccount>> {


    selectTargetAccount mActivity;
    //public final ProgressDialog dialog = new ProgressDialog(mActivity);
    public static final String list_url = "http://ec2-54-213-7-206.us-west-2.compute.amazonaws.com:8080/list_srvacc";

    getSAccountTask(selectTargetAccount mActivity){
        this.mActivity=mActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mActivity.dialog.setMessage("Loading Service account list...");
        //mActivity.dialog.show();
    }

    @Override
    protected List<serviceAccount> doInBackground(String... params) {
        List<serviceAccount> result = new ArrayList<serviceAccount>();

        try {
            URL u = new URL(list_url);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();
            InputStream is = conn.getInputStream();

            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(b) != -1)
                baos.write(b);

            String JSONResp = new String(baos.toByteArray());

            //Log.d("this is the json string",JSONResp);

            JSONObject arr = new JSONObject(JSONResp);
            JSONArray Jarray = arr.getJSONArray("lst");
            for (int i=0; i < Jarray.length(); i++) {
                result.add(convertContact(Jarray.getJSONObject(i)));
            }
            //for(int i=0;i<result.size();i++) Log.d("this is the shit",result.get(i).toString());

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<serviceAccount> result) {
        super.onPostExecute(result);
        //mActivity.dialog.dismiss();
        mActivity.adpt.setItemList(result);
        mActivity.adpt.notifyDataSetChanged();
    }
    private serviceAccount convertContact(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        String surname = obj.getString("srvID");
        //String email=obj.getString("email");
        return new serviceAccount(name, surname);
    }




}
