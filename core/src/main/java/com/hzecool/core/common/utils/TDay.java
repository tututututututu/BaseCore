package com.hzecool.core.common.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tutu on 2017/1/11.
 */
public class TDay implements Parcelable {

    public static final int WEEK_1 = 1;
    public static final int WEEK_2 = 2;
    public static final int WEEK_3 = 3;
    public static final int WEEK_4 = 4;
    public static final int WEEK_5 = 5;
    public static final int WEEK_6 = 6;
    public static final int WEEK_7 = 7;

    protected int year;
    protected int month;
    protected int day;
    protected int weekOf;

    public TDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setWeekOf(int weekOf) {
        this.weekOf = weekOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TDay TDay = (TDay) o;

        if (year != TDay.year) return false;
        if (month != TDay.month) return false;
        return day == TDay.day;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    /**
     * get the position of week ([1-7])
     */
    public int getWeekOf() {
        return weekOf;
    }

    @Override
    public String toString() {
        return "TDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", weekOf=" + weekOf +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.weekOf);
    }

    protected TDay(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.weekOf = in.readInt();
    }

    public static final Creator<TDay> CREATOR = new Creator<TDay>() {
        @Override
        public TDay createFromParcel(Parcel source) {
            return new TDay(source);
        }

        @Override
        public TDay[] newArray(int size) {
            return new TDay[size];
        }
    };
}
