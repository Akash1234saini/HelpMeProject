package com.example.akashsaini.helpme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class SavedNumber extends AppCompatActivity {

    ArrayList<String> savedNumberTitle = new ArrayList<>();
    ArrayList<String> savedNumber = new ArrayList<>();
    SwipeMenuListView listView;
    NumberAdapter adapter;
    SharedPreferences preferences;
    ArrayList<Number> savedNumbersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        listView = findViewById(R.id.listView);

        ArrayList<String> newSavedNumberTitle = new ArrayList<>();
        ArrayList<String> newSavedNumber = new ArrayList<>();

        try {

            newSavedNumberTitle = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("savedNumberTitles", ObjectSerializer.serialize(new ArrayList<String>())));
            newSavedNumber = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("savedNumbers", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SavedNumber.this, "Error to retrieve data", Toast.LENGTH_SHORT).show();
        }

        if (newSavedNumberTitle != null && newSavedNumber != null){
            if (newSavedNumberTitle.size() == newSavedNumber.size()){
                savedNumberTitle.addAll(newSavedNumberTitle);
                savedNumber.addAll(newSavedNumber);
            }
        }

        // Making instance of EmergencyNumber activity to call it methods
        EmergencyNumbers emergencyNumbers = new EmergencyNumbers();

        // remove duplicate values
        if (savedNumber != null && savedNumberTitle != null) {
            if (savedNumber.size() == savedNumberTitle.size()) {
                for (int i = 0; i < savedNumberTitle.size() - 1; i++) {
                    for (int j = i + 1; j < savedNumber.size(); j++) {
                        if (savedNumberTitle.get(i).equals(savedNumberTitle.get(j)))
                            savedNumberTitle.remove(j);
                        if (savedNumber.get(i).equals(savedNumber.get(j)))
                            savedNumber.remove(j);
                    }
                }
            }
        }

        savedNumbersArrayList = new ArrayList<>();
        if (savedNumberTitle.size() == savedNumber.size()){
            for (int i = 0; i < savedNumberTitle.size(); i++) {
                savedNumbersArrayList.add(new Number(emergencyNumbers.getLattersOfString(savedNumberTitle.get(i)), savedNumberTitle.get(i), savedNumber.get(i)));
            }
        } else{
            Toast.makeText(SavedNumber.this, "Error to retrieve data", Toast.LENGTH_SHORT).show();
        }

        adapter = new NumberAdapter(this, savedNumbersArrayList, false,false);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SavedNumber.this, "" + savedNumberTitle.get(position ), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
