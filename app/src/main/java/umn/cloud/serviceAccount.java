package umn.cloud;

/**
 * Created by AngusY on 3/29/15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class serviceAccount implements Serializable, Parcelable {

    private String name;
    private String srvId;
    private String email;

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
    public void writeToParcel(Parcel dest, int flag)
    {
        // TODO Auto-generated method stub
        dest.writeString(name);
        dest.writeString(srvId);
        //dest.writeInt(email);
    }







}