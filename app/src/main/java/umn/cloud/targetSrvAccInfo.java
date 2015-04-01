package umn.cloud;

/**
 * Created by AngusY on 3/31/15.
 */
public class targetSrvAccInfo {

    int tarSrvAccID;
    String time;

    public targetSrvAccInfo(int id, String time) {
        this.tarSrvAccID=id;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSrvId() {
        return tarSrvAccID;
    }

    public void setSrvId(int srvId) {
        this.tarSrvAccID = srvId;
    }


}
