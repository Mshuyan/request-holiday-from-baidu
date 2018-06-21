package com.shuyan;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( HolidayUtil.getHoliday(new Date(1524355200000L)) );
/*
    * status:   1：代表本来应该上班法定节假日改为休息，
    *           2：代表本来应该休息法定节假日改为上班
    * [
        {
            "date": "2018-4-5",
                "status": "1"
        },
        {
            "date": "2018-4-6",
                "status": "1"
        },
        {
            "date": "2018-4-7",
                "status": "1"
        },
        {
            "date": "2018-4-8",
                "status": "2"
        },
        {
            "date": "2018-4-28",
                "status": "2"
        },
        {
            "date": "2018-4-29",
                "status": "1"
        },
        {
            "date": "2018-4-30",
                "status": "1"
        }
  ]*/
    }
}
