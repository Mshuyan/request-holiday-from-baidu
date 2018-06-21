package com.shuyan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HolidayUtil {
    private static final String  baiduApiUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=%s&resource_id=6018";
    private static final OkHttpClient client = new OkHttpClient();

    public static List<JSONObject> getHoliday(Date date){
        String query = new SimpleDateFormat("yyyy年MM月").format(date);
        int paramMonth = Integer.parseInt(new SimpleDateFormat("yyyy-MM").format(date).split("-")[1]);
        String url = String.format(baiduApiUrl, query);

        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            Response response = client.newCall(request).execute();
            if ((!response.isSuccessful()) || (response.body() == null)) {
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("holiday");

            List<JSONObject> retList = new ArrayList<>();
            List<String> keyList = new ArrayList<>();

            for(int i=0; i<jsonArray.size(); i++){
                JSONArray list = jsonArray.getJSONObject(i).getJSONArray("list");
                for(int j=0; j<list.size(); j++){
                    JSONObject listObj = list.getJSONObject(j);
                    String s = listObj.getString("date");
                    int baiduMonth = Integer.parseInt(s.split("-")[1]);
                    if((!retList.contains(listObj) && (baiduMonth == paramMonth))){
                        retList.add(listObj);
                    }
                }
            }
            return retList;
        }catch (IOException e){
            return null;
        }
    }
}
