package com.jpushkarskaya.articles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by epushkarskaya on 10/19/16.
 */

@Parcel
public class Article {

    String webURL;
    String headline;
    String thumbNail;

    // Used for Parcel
    public Article() {
    }

    public Article(JSONObject object) {
        try {
            this.webURL = object.getString("web_url");
            this.headline = object.getJSONObject("headline").getString("main");

            JSONArray multimedia = object.getJSONArray("multimedia");
            if (multimedia.length() > 0){
                JSONObject multimediaObject = multimedia.getJSONObject(0);
                this.thumbNail = multimediaObject.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getWebURL() {
        return webURL;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail.isEmpty() ? thumbNail : String.format("http://www.nytimes.com/%s", thumbNail);
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                articles.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }
}
