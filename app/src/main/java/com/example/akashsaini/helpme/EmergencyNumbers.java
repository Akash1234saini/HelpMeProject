package com.example.akashsaini.helpme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class EmergencyNumbers extends AppCompatActivity {

    static ArrayList<Number> number;
    SwipeMenuListView listView;
    NumberAdapter adapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        final String[] numberTitleArray = getResources().getStringArray(R.array.numberTitleArray);
        String[] tempNumberArray = getResources().getStringArray(R.array.numberArray);
        final String[] numberArray = new String[tempNumberArray.length];

        numberArray[0] = tempNumberArray[0];
        for (int i = 1; i < tempNumberArray.length; i++) {
            numberArray[i] = tempNumberArray[i].replace("+ ", "");
        }

        // Shared Preferences to store order of array
        preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


        /** Clean a particular sharedPreferences
        * */
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("historyDate").apply();
//        editor.remove("historyTime").apply();
//        editor.remove("historyTitle").apply();
//        editor.remove("historyNumber").apply();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        // set data by method so we can call that method from AddSOS Activity
        setData(numberTitleArray, numberArray);
        adapter = new NumberAdapter(this, number, true, false);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // calling order method according to past changed order
        if (preferences.getString("set order", null) != null){
            setOrder(preferences.getString("set order", ""));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(preferences.getString("set order", ""), "Ascending"))
                    Arrays.sort(numberTitleArray);
                else
                    Arrays.sort(numberTitleArray, Collections.<String>reverseOrder());
            }
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // create "save" item
                SwipeMenuItem saveItem = new SwipeMenuItem(getApplicationContext());
                // set item width
                saveItem.setWidth(180);
                // set a icon
                saveItem.setIcon(R.drawable.ic_save_in_phone);
                // add to menu
                menu.addMenuItem(saveItem);

                // create "share" item
                SwipeMenuItem shareItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item width
                shareItem.setWidth((180));
                // set a icon
                shareItem.setIcon(R.drawable.share_icon);
                // add to menu
                menu.addMenuItem(shareItem);

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

                        Intent save = new Intent(Intent.ACTION_INSERT);
                        save.setType(ContactsContract.Contacts.CONTENT_TYPE);
                        save.putExtra(ContactsContract.Intents.Insert.NAME, number.get(position).getmNumberTitle());
                        save.putExtra(ContactsContract.Intents.Insert.PHONE, number.get(position).getmNumber());
                        startActivity(save);

                        // saving data in sharePreferences to display that data in savedNumber activity

                        // make arrayList to retrieve data and then extend that
                        ArrayList<String> Titles = new ArrayList<>();
                        ArrayList<String> numbers = new ArrayList<>();

                        try {

                            Titles = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("savedNumberTitles", ObjectSerializer.serialize(new ArrayList<String>())));
                            numbers = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("savedNumbers", ObjectSerializer.serialize(new ArrayList<String>())));

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        if (Titles != null && numbers != null){
                            if (Titles.size() == numbers.size()){
                                Titles.add(number.get(position).getmNumberTitle());
                                numbers.add(number.get(position).getmNumber());
                            }
                        }

                        try {

                            preferences.edit().putString("savedNumberTitles", ObjectSerializer.serialize(Titles)).apply();
                            preferences.edit().putString("savedNumbers", ObjectSerializer.serialize(numbers)).apply();

                        } catch (Exception e){
                            e.printStackTrace();
                        }

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
                                recentAction.add("Save number in phone");
                                Title.add(number.get(position).getmNumberTitle());
                                Number.add(number.get(position).getmNumber());
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

                        break;

                        // share this number to other
                    case 1:

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, number.get(position).getmNumberTitle() + "\n" + number.get(position).getmNumber());
                        share.setType("text/plain");
                        startActivity(Intent.createChooser(share, "send by app..."));

                        /**
                         // creating share preferences to retrieve and store data for recent activity */
                        recentAction = new ArrayList<>();
                        Title = new ArrayList<>();
                        Number = new ArrayList<>();
                        mDate = new ArrayList<>();
                        mTime = new ArrayList<>();

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
                        date = DateFormat.getDateInstance().format(new Date());
                        time = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

                        if (Title != null && Number != null && mDate != null && mTime != null && recentAction != null)
                            if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                                recentAction.add("Share number");
                                Title.add(number.get(position).getmNumberTitle());
                                Number.add(number.get(position).getmNumber());
                                mDate.add(date);
                                mTime.add(time);
//                        Log.i("currentDate", DateFormat.getDateInstance().format(new Date()));
//                        Log.i("currentTime", DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
                            }
                        Log.i("recentActions", "Recent: " + String.valueOf(recentAction) + "\nTitle: " + String.valueOf(Title) +
                                "\nNumber: " + String.valueOf(Number)
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

                        break;
                }
                return false;
            }
        });


        //set on click listener so that the hidden text will be appears when user click on that particular list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Number currentNumber = number.get(position);
                Toast.makeText(EmergencyNumbers.this, currentNumber.getmNumberTitle(), Toast.LENGTH_SHORT).show();


                // checking is permission granted or not
                if(ContextCompat.checkSelfPermission(EmergencyNumbers.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(EmergencyNumbers.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                else {
                    // calling intent
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", currentNumber.getmNumber(), null));
                    startActivity(callIntent);
                }


                ArrayList<String> Title = new ArrayList<>();
                ArrayList<String> Number = new ArrayList<>();
                ArrayList<String> mDate = new ArrayList<>();
                ArrayList<String> mTime = new ArrayList<>();

                // let's retrieve and store sharedPreferences for History data
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

                // create new date and time instance
                String date = DateFormat.getDateInstance().format(new Date());
                String time = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

                if (Title != null && Number != null && mDate != null && mTime != null)
                    if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                        Title.add(number.get(position).getmNumberTitle());
                        Number.add(number.get(position).getmNumber());
                        mDate.add(date);
                        mTime.add(time);
//                        Log.i("currentDate", DateFormat.getDateInstance().format(new Date()));
//                        Log.i("currentTime", DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
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

                /**
                // creating share preferences to retrieve and store data for recent activity */
                ArrayList<String> recentAction = new ArrayList<>();

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
                date = DateFormat.getDateInstance().format(new Date());

                if (Title != null && Number != null && mDate != null && mTime != null && recentAction != null)
                    if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                        recentAction.add("Last Call");
                        Title.add(number.get(position).getmNumberTitle());
                        Number.add(number.get(position).getmNumber());
                        mDate.add(date);
                        mTime.add(time);
//                        Log.i("currentDate", DateFormat.getDateInstance().format(new Date()));
//                        Log.i("currentTime", DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
                    }

                Log.i("recentActions", "Recent: " + String.valueOf(recentAction) + "\nTitle: " + String.valueOf(Title) +
                        "\nNumber: " + String.valueOf(Number)
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

            }
        });

    }


    // we are setting data here becz we will use this number in NumberAdapter class.
    public void setData(String[] numberTitleArray, String[] numberArray) {

        number = new ArrayList<>();

        for (int i = 0; i < numberTitleArray.length; i++) {
            number.add(new Number(getLattersOfString(numberTitleArray[i]), numberTitleArray[i], numberArray[i]));
        }
    }


    // this function will return the first later of first two words of emergency numbers.
    public String getLattersOfString(String s){

        String[] str = s.split(" ");
        StringBuilder builder = new StringBuilder(s.length());

        int numberOfWords;

        if (str.length > 2)
            numberOfWords = 2;
        else
            numberOfWords = str.length;

        for (int i = 0; i < numberOfWords; i++) {
            builder.append(str[i].toUpperCase().charAt(0));
        }

        return String.valueOf(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emergency_number_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for (Number newTitile : number) {
                    if (!newTitile.getmNumberTitle().equals(newText)) {
                        ArrayList<Number> isEmpty = new ArrayList<>();
                        adapter = new NumberAdapter(EmergencyNumbers.this, isEmpty, true,false);
                        listView.setAdapter(adapter);
                    }

                }
                if (TextUtils.isEmpty(newText)) {
                    adapter = new NumberAdapter(EmergencyNumbers.this, number, true, false);
                    listView.setAdapter(adapter);
                } else {
                    ArrayList<Number> filteredList = new ArrayList<>();

                    for (Number numbers : number) {
                        if (numbers.getmNumberTitle().toLowerCase().contains(newText.toLowerCase())) {
                            filteredList.add(numbers);
                            adapter = new NumberAdapter(EmergencyNumbers.this, filteredList, true,false);
                            listView.setAdapter(adapter);
                        }
                    }
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.ascending:
                preferences.edit().putString("set order", item.toString()).apply();
                setOrder(item.toString());
                break;
            case R.id.descending:
                preferences.edit().putString("set order", item.toString()).apply();
                setOrder(item.toString());
                break;
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

//    public NumberAdapter intilizeData(){
//
//    }

    public void setOrder(String order) {

        if (order.equals("Ascending")){
            Collections.sort(number, new Comparator<Number>() {
                @Override
                public int compare(Number o1, Number o2) {
                    return o1.getmNumberTitle().compareTo(o2.getmNumberTitle());
                }
            });
        } else if (order.equals("Descending")){
            Collections.sort(number, new Comparator<Number>() {
                @Override
                public int compare(Number o1, Number o2) {
                    return o2.getmNumberTitle().compareTo(o1.getmNumberTitle());
                }
            });
        }

    }
}
