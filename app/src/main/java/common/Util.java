package common;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static String uid = "";
    /**
     * @param jsonData
     * @return map
     * @desc 解析json串(需要根据不同的业务进行重写)
     */
    public static Map json_decode(String[] data, String jsonData)
    {
        String status = "";
        String code = "";
        String reason = "";
        String uid = "";
        Map<String, String> map = new HashMap<String, String>();
        try
        {
                JSONObject jsonObject = new JSONObject(jsonData);
                if (jsonObject != null) {
                    status = jsonObject.getString("status");
                    map.put("status", status);
                    uid = jsonObject.getString("uid");
                    map.put("uid", uid);
                    JSONObject bizData = jsonObject.getJSONObject("bizData");
                    if (bizData != null)  {
                        for (int i = 0; i < data.length; i++) {
                            map.put(data[i], bizData.getString(data[i]));
                        }
                    }
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        map.put("status", status);
//        map.put("code", code);
//        map.put("reason", reason);
        return map;
    }

    /**
     * @param map
     * @return String
     */
    public static String json_encode(Map<String, String> map)
    {
        JSONObject object = new JSONObject(map);
        return object.toString();
    }

    public static String sendJsonPost(String Json, String urlPath) {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                Log.d("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String[] getResult(String jsonData) {
        String[] result = {};
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject != null) {
                JSONObject bizData = jsonObject.getJSONObject("bizData");
                if(bizData != null) {
                    System.err.println("biaData:" +bizData);
                    JSONArray resultTmp = bizData.getJSONArray("result");
                    result = new String[resultTmp.length()];
                    for(int j = 0;j < resultTmp.length();j++){
                        JSONObject resultTmpJSONObject = resultTmp.getJSONObject(j);
                        System.err.println("resultTmpJSONObject:" +resultTmpJSONObject);
                        result[j] = resultTmpJSONObject.getString("did");
                        System.err.println("result" +j+":"+result);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.err.println("result:" +result);
        return result;
    }
    public static String[][] getResult2(String jsonData) {
        String[][] result  = {};
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject != null) {
                JSONObject bizData = jsonObject.getJSONObject("bizData");
                if(bizData != null) {
                    System.err.println("bizData:" +bizData);
                    JSONArray resultTmp = bizData.getJSONArray("result");
                    System.err.println("result.LENGTH:"+resultTmp.length());
                    result = new String[resultTmp.length()][3];
                    for(int j = 0;j < resultTmp.length();j++){
                        JSONObject resultTmpJSONObject = resultTmp.getJSONObject(j);
                        System.err.println("resultTmpJSONObject:" +resultTmpJSONObject);
                        result[j][0] = resultTmpJSONObject.getString("did");
                        result[j][1] = resultTmpJSONObject.getString("temperature");
                        result[j][2] = resultTmpJSONObject.getString("update_time");
                       // result.put("did",resultTmpJSONObject.getString("did"));
                        //result.put("did",resultTmpJSONObject.getString("did"));
                        //result.put("did",resultTmpJSONObject.getString("did"));
                        System.err.println("result" +j+":"+result);
                    }
                    System.err.println("result.LENGTH:"+result.length);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String[] getTem(String jsonData) {
        String[] temperature = {};
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject != null) {
                JSONObject bizData = jsonObject.getJSONObject("bizData");
                if(bizData != null) {
                    System.err.println("bizData:" +bizData);
                    JSONArray resultTmp = bizData.getJSONArray("temperature");
                    temperature = new String[resultTmp.length()];
                    for(int j = 0;j < resultTmp.length();j++){
                        JSONObject resultTmpJSONObject = resultTmp.getJSONObject(j);
                        System.err.println("resultTmpJSONObject:" +resultTmpJSONObject);
                        temperature[j] = resultTmpJSONObject.getString("tem");
                        System.err.println("temperature" +j+":"+temperature);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.err.println("temperature:" +temperature);
        return temperature;
    }
    public static String[] getTD(String jsonData) {
        String[] timeDate = {};
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject != null) {
                JSONObject bizData = jsonObject.getJSONObject("bizData");
                if(bizData != null) {
                    System.err.println("bizData:" +bizData);
                    JSONArray resultTmp = bizData.getJSONArray("timeDate");
                    timeDate = new String[resultTmp.length()];
                    for(int j = 0;j < resultTmp.length();j++){
                        JSONObject resultTmpJSONObject = resultTmp.getJSONObject(j);
                        System.err.println("resultTmpJSONObject:" +resultTmpJSONObject);
                        timeDate[j] = resultTmpJSONObject.getString("update_time");
                        System.err.println("timeDate" +j+":"+timeDate);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.err.println("timeDate:" +timeDate);
        return timeDate;
    }
}