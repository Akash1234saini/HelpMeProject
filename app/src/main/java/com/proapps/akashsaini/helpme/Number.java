package com.proapps.akashsaini.helpme;

class Number {

    private String mNumberIcon;
    private String mNumberTitle;
    private String mNumber;

    // added new branch.
    // new commant of new branch.

    Number (String numberIcon, String numberTitle, String number) {
        mNumberIcon = numberIcon;
        mNumberTitle = numberTitle;
        mNumber = number;
    }

    String getmNumberIcon() {
        return mNumberIcon;
    }

    String getmNumberTitle() {
        return mNumberTitle;
    }

    String getmNumber() {
        return mNumber;
    }
}