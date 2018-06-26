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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class HolidayUtil {
    private static final String baiduApiUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=%s&resource_id=6018";
    private static final OkHttpClient client = new OkHttpClient();

    //从百度API中获取1个月的节假日
    public static List<JSONObject> getHoliday(Date date) {
        String query = new SimpleDateFormat("yyyy年MM月").format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int paramMonth = cal.get(Calendar.MONTH) + 1;
        String url = String.format(baiduApiUrl, query);

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if ((!response.isSuccessful()) || (response.body() == null)) {
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
            List<JSONObject> jsonObjectList = new ArrayList<>();

            try {
                JSONObject obj;
                if ((obj = data.getJSONObject("holiday")) != null) {
                    jsonObjectList.add(obj);
                }
            } catch (ClassCastException ignored) {

            }

            try {
                JSONArray jsonArr;
                if ((jsonArr = data.getJSONArray("holiday")) != null) {
                    jsonObjectList = jsonArr.toJavaList(JSONObject.class);
                }
            } catch (ClassCastException ignored) {

            }

            List<JSONObject> holidayList = new ArrayList<>();

            for (JSONObject aJsonObjectList : jsonObjectList) {
                JSONArray list = aJsonObjectList.getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject listObj = list.getJSONObject(j);
                    String s = listObj.getString("date");
                    int baiduMonth = Integer.parseInt(s.split("-")[1]);
                    if ((!holidayList.contains(listObj) && (baiduMonth == paramMonth))) {
                        holidayList.add(listObj);
                    }
                }
            }
            return holidayList;
        } catch (IOException e) {
            return null;
        }
    }

    //将节假日和周末进行整合，得到本月中所有需要放假的日期
    public static List<String> getWeekday(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        int paramMonth = cal.get(Calendar.MONTH) + 1;

        List<JSONObject> holidayList = getHoliday(date);
        if (holidayList == null)
            return null;
        List<String> weekList = new ArrayList<>();
        while ((cal.get(Calendar.MONTH) + 1) == paramMonth) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
                boolean flag = true;
                for (JSONObject obj : holidayList) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    if (sameDate(format.parse(obj.getString("date")), cal.getTime()) && obj.getInteger("status").equals(2)) {
                        flag = false;
                    }
                }
                if (flag)
                    weekList.add(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
            } else {
                for (JSONObject obj : holidayList) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    if (sameDate(format.parse(obj.getString("date")), cal.getTime()) && obj.getInteger("status").equals(1)) {
                        weekList.add(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    }
                }
            }
            cal.add(Calendar.DATE, 1);
        }
        return weekList;
    }

    //忽略时分秒比较日期是否相等
    private static boolean sameDate(Date d1, Date d2) {
        LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}
