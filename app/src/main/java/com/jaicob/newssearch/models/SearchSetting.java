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
    String query;
    String fq;
    ArticleLength articleLength;

    public SearchSetting(){
        this.sort = NEWEST;
        this.beginDateRange = BeginDateRange.UNSET;
        this.query = "";
        this.fq = "type_of_material:\"News\"";
        this.articleLength = ArticleLength.ANY;
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

    public String getFq() {
        return this.fq;
    }

    public void setFq(String fq){
        this.fq = fq;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArticleLength getArticleLength() {
        return articleLength;
    }

    public void setArticleLength(ArticleLength articleLength) {
        this.articleLength = articleLength;
    }
}
