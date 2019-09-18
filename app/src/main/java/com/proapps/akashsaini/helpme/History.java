package com.proapps.akashsaini.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    ArrayList<NumberOfRecentAndHistory> number = new ArrayList<>();
    SwipeMenuListView listView;
    NumberAdapterOfRecentAndHistory adapter;
    SharedPreferences preferences;
    ArrayList<String> mTitle = new ArrayList<>();
    ArrayList<String> mNumber = new ArrayList<>();
    ArrayList<String> mDate = new ArrayList<>();
    ArrayList<String> mTime = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        ArrayList<String> Title = new ArrayList<>();
        ArrayList<String> Number = new ArrayList<>();
        ArrayList<String> Date = new ArrayList<>();
        ArrayList<String> Time = new ArrayList<>();

        try {

            Title = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyTitle",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Number = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyNumber",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Date = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyDate",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Time = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyTime",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Title != null && Number != null && Date != null && Time != null){
            if (Title.size() == Number.size() && Date.size() == Time.size()){
                mTitle.addAll(Title);
                mNumber.addAll(Number);
                mDate.addAll(Date);
                mTime.addAll(Time);
            }
        }

        EmergencyNumbers emergencyNumbers = new EmergencyNumbers();

        if (mTitle.size() == 0 &&  mNumber.size() == 0 && mDate.size() == 0 && mTime.size() == 0)
            Toast.makeText(this, "No history available", Toast.LENGTH_LONG).show();
        else if (mTitle != null && mNumber != null)
            if (mTime.size() == mNumber.size() && mTime.size() == mTitle.size() && mTitle.size() == mDate.size())
                for (int i = 0; i < mTitle.size(); i++)
                    number.add(new NumberOfRecentAndHistory(emergencyNumbers.getLattersOfString(mTitle.get(i)), mTitle.get(i), mNumber.get(i), mDate.get(i), mTime.get(i)));
        else
                Toast.makeText(this, "Error retrieve history data", Toast.LENGTH_LONG).show();


        listView = findViewById(R.id.listView);
        adapter = new NumberAdapterOfRecentAndHistory(this, number, false);
        listView.setAdapter(adapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item width
                deleteItem.setWidth((180));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_listview_item);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };

        // set creator
        listView.setMenuCreator(creator);

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    // save this number in phone
                    case 0:
                        ArrayList<String> Title = new ArrayList<>();
                        ArrayList<String> Number = new ArrayList<>();
                        ArrayList<String> mDate = new ArrayList<>();
                        ArrayList<String> mTime = new ArrayList<>();

                        // let's retrieve and delete sharedPreferences for History data
                        try {

                            Title = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyTitle",
                                    ObjectSerializer.serialize(new ArrayList<String>())));

                            Number = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyNumber",
                                    ObjectSerializer.serialize(new ArrayList<String>())));

                            mDate = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyDate",
                                    ObjectSerializer.serialize(new ArrayList<String>())));

                            mTime = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("historyTime",
                                    ObjectSerializer.serialize(new ArrayList<String>())));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // delete data
                        if (Title != null && Number != null && mDate != null && mTime != null)
                            if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                                Title.remove(position);
                                Number.remove(position);
                                mDate.remove(position);
                                mTime.remove(position);
                            }

                        // store call data for history Activity
                        try {

                            preferences.edit().putString("historyTitle",
                                    ObjectSerializer.serialize(Title)).apply();

                            preferences.edit().putString("historyNumber",
                                    ObjectSerializer.serialize(Number)).apply();

                            preferences.edit().putString("historyDate",
                                    ObjectSerializer.serialize(mTime)).apply();


                            preferences.edit().putString("historyTime",
                                    ObjectSerializer.serialize(mDate)).apply();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(History.this, MainActivity.class);
                        startActivity(intent);

                        break;
                }
                return false;
            }
        });



    }


}
