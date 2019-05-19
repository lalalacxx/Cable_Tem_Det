package common;

import android.app.Application;

/**
 * Created by cxx on 2019/5/12.
 */
public class MyApplication extends Application {
    private String uid = "";

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return this.uid;
    }
}
