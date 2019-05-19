package common;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by cxx on 2019/5/19.
 */
public class SerializableMap implements Serializable {
    private String[][] data;
    public String[][] getData()
    {
        return data;
    }
    public void setData(String[][] a)
    {
        this.data=a;
    }
    public int size()
    {
        return data.length;
    }
    public String showData(int i,int j)
    {
        return data[i][j];
    }
}
