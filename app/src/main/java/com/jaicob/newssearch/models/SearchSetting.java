package com.jaicob.newssearch.models;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jaicob on 7/30/16.
 */

@Parcel
public class SearchSetting {
    public static final String OLDEST = "oldest";
    public static final String NEWEST = "newest";

    String sort;
    BeginDateRange beginDateRange;

    public SearchSetting(){
        this.sort = NEWEST;
        this.beginDateRange = BeginDateRange.UNSET;
    }

    public String getBeginDate() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yMMdd");

        switch (beginDateRange){
            case THIS_WEEK:
                cal.add(Calendar.DATE, -7);
                break;
            case THIS_MONTH:
                cal.add(Calendar.MONTH, -1);
                break;
            case THIS_YEAR:
                cal.add(Calendar.YEAR, -1);
                break;
            case UNSET:
                return "";
        }

        String beginDate = dateFormat.format(cal.getTime());
        return beginDate;
    }

    public BeginDateRange getBeginDateRange() {
        return beginDateRange;
    }

    public void setBeginDateRange(BeginDateRange range) {
        beginDateRange = range;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
