package com.jaicob.newssearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jaicob on 7/27/16.
 */
public class Article {

    String webUrl;
    String headline;
    String thumbnail;
    String pubDate;
    String wordCount;


    public Article(JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.pubDate = jsonObject.getString("pub_date");
            this.wordCount = jsonObject.getString("word_count");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");


            if (multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                for (int i = 0; i < multimedia.length(); i++) {
                    JSONObject image = multimedia.getJSONObject(i);
                    if (image.getString("subtype").equals("xlarge")) {
                        multimediaJson = image;
                    }
                }
                this.thumbnail = "http://www.nytimes.com/" +  multimediaJson.getString("url");
            } else {
                this.thumbnail = "";
            }


        } catch (JSONException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Article a = new Article(jsonArray.getJSONObject(i));
                articles.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }


    public String getWebUrl() {
        return webUrl;
    }


    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }


    public String getHeadline() {
        return headline;
    }


    public void setHeadline(String headline) {
        this.headline = headline;
    }


    public String getThumbnail() {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getPubDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(this.pubDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newFormat.format(cal.getTime());
    }


    public String getReadTime() {
        // Average Words per minute is approx. 200
        int avgWpm = 200;
        int readTime = (int) Math.ceil(Integer.parseInt(wordCount)/avgWpm);
        String res = "Read Time: " + String.valueOf(readTime) + " Minutes";
        if (readTime < 1) {
            return "Read Time: Less than a minute";
        }
        return  res;
    }
}
