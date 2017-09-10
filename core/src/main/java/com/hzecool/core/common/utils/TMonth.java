package com.hzecool.core.common.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tutu on 2017/1/11.
 */
public class TMonth implements Parcelable {

    public static final int SUNDAY_OF_WEEK = 1;
    public static final int MONDAY_OF_WEEK = 2;
    public static final int SATURDAY_OF_WEEK = 7;
    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    protected int year;
    protected int month;
    protected List<TDay> selectedDays = new ArrayList<>(5);
    private TDay[][] monthDays = new TDay[ROW_COUNT][COL_COUNT];
    private int realRowCount;
    private int firstDayOfWeek;
    private int firstdayOfWeekPosInMonth;
    private int dayCountOfPrevMonth;
    private TMonth prevMonth;
    private TMonth nextMonth;

    public TMonth(int year, int month, @WeekType int firstDayOfWeek) {
        this.year = year;
        this.month = month;
        this.firstDayOfWeek = firstDayOfWeek;
        calculateDays();
    }

    TMonth(int year, int month) {
        this.year = year;
        this.month = month;
    }

    private void calculateDays() {

        prevMonth = TDateUtils.prevMonth(getYear(), getMonth());
        dayCountOfPrevMonth = TDateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        nextMonth = TDateUtils.nextMonth(getYear(), getMonth());
        // the first day of month is which week's day
        firstdayOfWeekPosInMonth = TDateUtils.mapDayOfWeekInMonth(TDateUtils.getDayOfWeekInMonth(getYear(), getMonth()), firstDayOfWeek);
        int dayCountOfMonth = TDateUtils.getDayCountOfMonth(getYear(), getMonth());

        int currentRealRowCount = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++) {
            boolean isAllRowEmpty = true;
            for (int col = 1; col <= COL_COUNT; col++) {
                int monthPosition = col + row * COL_COUNT;
                TDay oldFullDay = monthDays[row][col - 1];
                if (monthPosition >= firstdayOfWeekPosInMonth
                        && monthPosition < firstdayOfWeekPosInMonth + dayCountOfMonth) { // current month
                    if (oldFullDay == null) {
                        TDay currentMonthDay = new TDay(getYear(), getMonth(), day);
                        monthDays[row][col - 1] = currentMonthDay;
                    } else {
                        oldFullDay.setYear(getYear());
                        oldFullDay.setMonth(getMonth());
                        oldFullDay.setDay(day);
                    }
                    day++;
                    isAllRowEmpty = false;
                } else if (monthPosition < firstdayOfWeekPosInMonth) { // prev month
                    int prevDay = dayCountOfPrevMonth - (firstdayOfWeekPosInMonth - 1 - monthPosition);
                    if (oldFullDay == null) {
                        TDay prevMonthDay = new TDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                        monthDays[row][col - 1] = prevMonthDay;
                    } else {
                        oldFullDay.setYear(prevMonth.getYear());
                        oldFullDay.setMonth(prevMonth.getMonth());
                        oldFullDay.setDay(prevDay);
                    }
                } else if (monthPosition >= firstdayOfWeekPosInMonth + dayCountOfMonth) { // next month
                    if (oldFullDay == null) {
                        TDay nextMonthDay = new TDay(nextMonth.getYear(), nextMonth.getMonth(),
                                monthPosition - (firstdayOfWeekPosInMonth + dayCountOfMonth) + 1);
                        monthDays[row][col - 1] = nextMonthDay;
                    } else {
                        oldFullDay.setYear(nextMonth.getYear());
                        oldFullDay.setMonth(nextMonth.getMonth());
                        oldFullDay.setDay(monthPosition - (firstdayOfWeekPosInMonth + dayCountOfMonth) + 1);
                    }
                }
                monthDays[row][col - 1].setWeekOf(col);
            }
            if (!isAllRowEmpty) {
                currentRealRowCount++;
            }
        }

        setRealRowCount(currentRealRowCount);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SUNDAY_OF_WEEK, MONDAY_OF_WEEK, SATURDAY_OF_WEEK})
    public @interface WeekType {
    }

    void setMonthDays(TDay[][] monthDays) {
        this.monthDays = monthDays;
    }

    public TMonth getNextMonth() {
        return nextMonth;
    }

    public TMonth getPrevMonth() {
        return prevMonth;
    }

    public int getDayCountOfPrevMonth() {
        return dayCountOfPrevMonth;
    }

    public int getFirstdayOfWeekPosInMonth() {
        return firstdayOfWeekPosInMonth;
    }

    public TDay[][] getMonthDays() {
        return monthDays;
    }

    public void setRealRowCount(int realRowCount) {
        this.realRowCount = realRowCount;
    }

    public int getRealRowCount() {
        return realRowCount;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setSelectedDays(List<TDay> selectedDays) {
        this.selectedDays = selectedDays;
    }

    protected List<TDay> getSelectedDays() {
        return selectedDays;
    }

    public void addSelectedDay(TDay TDay) {
        getSelectedDays().add(TDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TMonth TMonth = (TMonth) o;
        if (year != TMonth.year) return false;
        return month == TMonth.month;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }

    @Override
    public String toString() {
        return year + "-" + month;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeTypedList(selectedDays);
    }

    protected TMonth(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.selectedDays = in.createTypedArrayList(TDay.CREATOR);
        calculateDays();
    }

    public static final Creator<TMonth> CREATOR = new Creator<TMonth>() {
        @Override
        public TMonth createFromParcel(Parcel source) {
            return new TMonth(source);
        }

        @Override
        public TMonth[] newArray(int size) {
            return new TMonth[size];
        }
    };
}