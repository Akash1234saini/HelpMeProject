package com.example.akashsaini.helpme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuickAccessFragment extends Fragment {

    NumberAdapter adapter;
    SwipeMenuListView listView;
    ArrayList<Number> quickAccessibleNumbers;

    public QuickAccessFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view, container, false);

        quickAccessibleNumbers = new ArrayList<>();

        ArrayList<String> newNumbers = null;
        ArrayList<String> newNumberTitleArray = null;

        final SharedPreferences preferences = getActivity().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);

        /** Clean a particular sharedPreferences
         * */
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("recentAction").apply();
//        editor.remove("recentTitle").apply();
//        editor.remove("recentNumber").apply();
//        editor.remove("recentDate").apply();
//        editor.remove("recentTime").apply();

        try {

            newNumbers = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("quickAccessibleNumbers",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            newNumberTitleArray = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("quickAccessibleNumberTitles",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        EmergencyNumbers emergencyNumbers = new EmergencyNumbers();

        // remove duplicate values
        if (newNumbers != null && newNumberTitleArray != null) {
            if (newNumbers.size() == newNumberTitleArray.size()) {
                for (int i = 0; i < newNumberTitleArray.size() - 1; i++) {
                    for (int j = i + 1; j < newNumbers.size(); j++) {
                        if (newNumberTitleArray.get(i).equals(newNumberTitleArray.get(j)))
                            newNumberTitleArray.remove(j);
                        if (newNumbers.get(i).equals(newNumbers.get(j)))
                            newNumbers.remove(j);

                    }
                }

                Log.i("setsStr", "" + String.valueOf(newNumbers) + " " + (newNumbers.size() == newNumberTitleArray.size()));

                for (int i = 0; i < newNumbers.size(); i++) {
                    quickAccessibleNumbers.add(new Number(emergencyNumbers.getLattersOfString(newNumberTitleArray.get(i)), newNumberTitleArray.get(i), newNumbers.get(i)));
                }
            }
        }
                adapter = new NumberAdapter(getActivity(), quickAccessibleNumbers, false, true);
                listView = rootView.findViewById(R.id.listView);
                listView.setAdapter(adapter);


        //set on click listener so that the hidden text will be appears when user click on that particular list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Number currentNumber = quickAccessibleNumbers.get(position);
                Toast.makeText(getContext(), getString(R.string.calling_intent_start), Toast.LENGTH_SHORT).show();


                // checking is permission granted or not
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, 1);
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
                        Title.add(quickAccessibleNumbers.get(position).getmNumberTitle());
                        Number.add(quickAccessibleNumbers.get(position).getmNumber());
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
                time = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

                if (Title != null && Number != null && mDate != null && mTime != null && recentAction != null)
                    if (Title.size() == Number.size() && mDate.size() == mTime.size()) {
                        recentAction.add("Last Call");
                        Title.add(quickAccessibleNumbers.get(position).getmNumberTitle());
                        Number.add(quickAccessibleNumbers.get(position).getmNumber());
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

            }
        });


        return rootView;
    }
}
