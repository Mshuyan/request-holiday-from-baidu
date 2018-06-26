package com.shuyan;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(1514736000000L));
        for(int i=0; i<12; i++){
            System.out.println( HolidayUtil.getWeekday(cal.getTime()));
            cal.add(Calendar.MONTH,1);
        }
    }
}
