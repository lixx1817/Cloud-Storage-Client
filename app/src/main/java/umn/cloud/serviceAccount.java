package umn.cloud;

/**
 * Created by AngusY on 3/29/15.
 */

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class serviceAccount implements Serializable, Parcelable {

    private String name;
    private String srvId;
    private String email;

    private static final String KEY_NAME = "name";
    private static final String KEY_SRVID = "srvId";
    private static final String KEY_EMAIL="email";

    public serviceAccount(String name, String srvId, String email) {
        super();
        this.name = name;
        this.srvId = srvId;
        this.email = email;
    }
    public serviceAccount(String name, String srvId) {
        super();
        this.name = name;
        this.srvId = srvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrvId() {
        return srvId;
    }

    public void setSrvId(String srvId) {
        this.srvId = srvId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        // insert the key value pairs to the bundle
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_SRVID, srvId);
        bundle.putString(KEY_EMAIL, email);

        // write the key value pairs to the parcel
        dest.writeBundle(bundle);
    }

    /**
     * Creator required for class implementing the parcelable interface.
     */
    public static final Parcelable.Creator<serviceAccount> CREATOR = new Creator<serviceAccount>() {

        @Override
        public serviceAccount createFromParcel(Parcel source) {
            // read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // instantiate a person using values from the bundle
            return new serviceAccount(bundle.getString(KEY_NAME),
                    bundle.getString(KEY_EMAIL),bundle.getString(KEY_SRVID));
        }

        @Override
        public serviceAccount[] newArray(int size) {
            return new serviceAccount[size];
        }

    };







}