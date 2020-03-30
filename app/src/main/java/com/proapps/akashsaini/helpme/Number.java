package com.proapps.akashsaini.helpme;

class Number {

    private String mNumberTitle;
    private String mNumber;
    private String mStateOrTerritory;

    // added new branch.
    // new commant of new branch.

    Number ( String numberTitle, String number, String stateOrTerritory) {
        mNumberTitle = numberTitle;
        mNumber = number;
        mStateOrTerritory = stateOrTerritory;
    }

    String getmNumberTitle() {
        return mNumberTitle;
    }

    String getmNumber() {
        return mNumber;
    }

    String getmStateOrTerritory() {
        return mStateOrTerritory;
    }
}
