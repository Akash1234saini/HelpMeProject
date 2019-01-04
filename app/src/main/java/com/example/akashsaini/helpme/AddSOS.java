package com.example.akashsaini.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class AddSOS extends AppCompatActivity implements  AdapterView.OnItemClickListener {

    SwipeMenuListView listView;
    NumberAdapter adapter;

    String[] numberTitleArray;
    String[] numberArray;

    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        numberTitleArray = getResources().getStringArray(R.array.numberTitleArray);
        String[] tempNumberArray = getResources().getStringArray(R.array.numberArray);
        numberArray = new String[tempNumberArray.length];

        numberArray[0] = tempNumberArray[0];
        for (int i = 1; i < tempNumberArray.length; i++) {
            numberArray[i] = tempNumberArray[i].replace("+ ", "");
        }

        preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        EmergencyNumbers emergencyNumbers = new EmergencyNumbers();

        emergencyNumbers.setData(numberTitleArray, numberArray);


        adapter = new NumberAdapter(this, EmergencyNumbers.number, true, false);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // calling order method according to past changed order
        if (preferences.getString("set order", null) != null) {
            emergencyNumbers.setOrder(preferences.getString("set order", ""));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(preferences.getString("set order", ""), "Ascending"))
                    Arrays.sort(numberTitleArray);
                else
                    Arrays.sort(numberTitleArray, Collections.<String>reverseOrder());
            }
        }

        // calling public list click listener
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();

        /**
         // creating share preferences to retrieve and store data for recent activity */
        ArrayList<String> recentAction = new ArrayList<>();
        ArrayList<String> Title = new ArrayList<>();
        ArrayList<String> Number = new ArrayList<>();
        ArrayList<String> mDate = new ArrayList<>();
        ArrayList<String> mTime = new ArrayList<>();

        try {

            recentAction = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentAction",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Title = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentTitle",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Number = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentNumber",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            mDate = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentDate",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            mTime = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentTime",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create new date and time instance
        String date = DateFormat.getDateInstance().format(new Date());
        String time = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        if (Title != null && Number != null && mDate != null && mTime != null && recentAction != null)
            if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                recentAction.add("Add new SOS");
                Title.add(numberTitleArray[position]);
                Number.add(EmergencyNumbers.number.get(position)
                        .getmNumber());
                mDate.add(date);
                mTime.add(time);
//                        Log.i("currentDate", DateFormat.getDateInstance().format(new Date()));
//                        Log.i("currentTime", DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
            }

        Log.i("recentActions", "Recent: " + String.valueOf(recentAction) + "\nTitle: " + String.valueOf(Title) + "\nNumber: " + String.valueOf(Number)
                + "\nDate: " + String.valueOf(mDate) + "\nTime: " + String.valueOf(mTime));

        // store call data for recent Activity
        try {

            preferences.edit().putString("recentAction",
                    ObjectSerializer.serialize(recentAction)).apply();

            preferences.edit().putString("recentTitle",
                    ObjectSerializer.serialize(Title)).apply();

            preferences.edit().putString("recentNumber",
                    ObjectSerializer.serialize(Number)).apply();

            preferences.edit().putString("recentDate",
                    ObjectSerializer.serialize(mTime)).apply();

            preferences.edit().putString("recentTime",
                    ObjectSerializer.serialize(mDate)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }



        ArrayList<String> saveNumberTitleForQucikAccessActivity = new ArrayList<>();
        ArrayList<String> saveNumbersForQucikAccessActivity = new ArrayList<>();

        saveNumbersForQucikAccessActivity.clear();
        saveNumberTitleForQucikAccessActivity.clear();

        try {

            saveNumbersForQucikAccessActivity = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("quickAccessibleNumbers",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            saveNumberTitleForQucikAccessActivity = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("quickAccessibleNumberTitles",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (saveNumbersForQucikAccessActivity != null && saveNumberTitleForQucikAccessActivity != null) {
            saveNumberTitleForQucikAccessActivity.add(numberTitleArray[position]);
            saveNumbersForQucikAccessActivity.add(EmergencyNumbers.number.get(position)
                    .getmNumber());
        }



        try {

            preferences.edit().putString("quickAccessibleNumberTitles",
                    ObjectSerializer.serialize(saveNumberTitleForQucikAccessActivity)).apply();

            preferences.edit().putString("quickAccessibleNumbers",
                    ObjectSerializer.serialize(saveNumbersForQucikAccessActivity)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}