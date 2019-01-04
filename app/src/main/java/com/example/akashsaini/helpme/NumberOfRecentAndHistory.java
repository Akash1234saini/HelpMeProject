package com.example.akashsaini.helpme;

public class NumberOfRecentAndHistory {
    private String mTitleLatters;
    private String mTitle;
    private String mNumber;
    private String mDate;
    private String mTime;
    private String recentActivity;

    NumberOfRecentAndHistory(String mTitleLatters, String mTitle, String mNumber, String mDate, String mTime) {
        this.mTitleLatters = mTitleLatters;
        this.mTitle = mTitle;
        this.mNumber = mNumber;
        this.mDate = mDate;
        this.mTime = mTime;
    }

    public NumberOfRecentAndHistory(String mTitleLatters, String mTitle, String mNumber, String mDate, String mTime, String recentActivity) {
        this.mTitleLatters = mTitleLatters;
        this.mTitle = mTitle;
        this.mNumber = mNumber;
        this.mDate = mDate;
        this.mTime = mTime;
        this.recentActivity = recentActivity;
    }

    String getmTitleLatters() {
        return mTitleLatters;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmNumber() {
        return mNumber;
    }

    String getmDate() {
        return mDate;
    }

    String getmTime() {
        return mTime;
    }

    public String getRecentActivity() {
        return recentActivity;
    }
}
