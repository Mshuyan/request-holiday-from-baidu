package com.shuyan;

import java.text.ParseException;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException {
        System.out.println( HolidayUtil.getWeekday(new Date(1524355200000L)) );
    }
}
