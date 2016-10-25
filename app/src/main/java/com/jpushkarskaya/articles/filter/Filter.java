package com.jpushkarskaya.articles.filter;

import com.loopj.android.http.RequestParams;

import org.parceler.Parcel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by epushkarskaya on 10/22/16.
 */

@Parcel
public class Filter {

    boolean customDate;
    int beginYear;
    int beginMonth;
    int beginDay;
    int sortOrder;
    boolean artsChecked;
    boolean styleChecked;
    boolean sportsChecked;

    // Used for Parcel
    public Filter() {
    }

    public Filter setDate(int year, int month, int day) {
        this.customDate = true;
        this.beginYear = year;
        this.beginMonth = month;
        this.beginDay = day;
        return this;
    }

    public void setOrder(int order) {
        this.sortOrder = order;
    }

    public void setDesks(boolean arts, boolean style, boolean sports) {
        this.artsChecked = arts;
        this.styleChecked = style;
        this.sportsChecked = sports;
    }

    public int getBeginYear() {
        return beginYear;
    }

    public int getBeginMonth() {
        return beginMonth;
    }

    public int getBeginDay() {
        return beginDay;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isArtsChecked() {
        return artsChecked;
    }

    public boolean isStyleChecked() {
        return styleChecked;
    }

    public boolean isSportsChecked() {
        return sportsChecked;
    }

    public boolean isDateCustom() {
        return customDate;
    }

    public static RequestParams applyFilter(Filter filter, RequestParams params) {
        if (filter == null ) {
            return params;
        }

        if (filter.isDateCustom()) {
            params.put("begin_date", getDate(filter));
        }

        if (filter.getSortOrder() == 0) {
            params.put("sort", "newest");
        } else if (filter.getSortOrder() == 1) {
            params.put("sort", "oldest");
        } else {
            // invalid sort order. do not apply filter.
        }

        Set<String> newsDesks = new HashSet<>();
        if (filter.isArtsChecked()) {
            newsDesks.add("Arts");
        }
        if (filter.isStyleChecked()) {
            newsDesks.add("Fashion & Style");
        }
        if (filter.isSportsChecked()) {
            newsDesks.add("Sports");
        }

        if (newsDesks.size() > 0 ){
            StringBuilder queryValue = new StringBuilder();
            queryValue.append("news_desk:(");
            for (String desk : newsDesks) {
                queryValue.append("\"");
                queryValue.append(desk);
                queryValue.append("\" ");
            }
            queryValue.delete(queryValue.length()-2, queryValue.length());
            queryValue.append(")");
            params.put("fq", newsDesks);
        }

        return params;
    }

    private static String getDate(Filter filter) {
        String day = String.valueOf(filter.getBeginDay());
        String month = String.valueOf(filter.getBeginMonth());
        if (filter.getBeginDay() < 10) {
            day = "0" + day;
        }

        if (filter.getBeginMonth() < 10){
            month = "0" + month;
        }
        return String.format("%s%s%s", filter.getBeginYear(), month, day);
    }
}
