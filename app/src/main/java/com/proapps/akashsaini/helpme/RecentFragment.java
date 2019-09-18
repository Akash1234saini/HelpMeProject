package com.proapps.akashsaini.helpme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;

public class RecentFragment extends Fragment {

    ArrayList<NumberOfRecentAndHistory> number = new ArrayList<>();
    SwipeMenuListView listView;
    NumberAdapterOfRecentAndHistory adapter;
    ArrayList<String> mTitle = new ArrayList<>();
    ArrayList<String> mNumber = new ArrayList<>();
    ArrayList<String> mDate = new ArrayList<>();
    ArrayList<String> mTime = new ArrayList<>();
    ArrayList<String> mRecentAction = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view, container, false);

        final SharedPreferences preferences = getActivity().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);

        ArrayList<String> recentAction = new ArrayList<>();
        ArrayList<String> Title = new ArrayList<>();
        ArrayList<String> Number = new ArrayList<>();
        ArrayList<String> Date = new ArrayList<>();
        ArrayList<String> Time = new ArrayList<>();

        try {

            recentAction = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentAction",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Title = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentTitle",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Number = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentNumber",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Date = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentDate",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            Time = (ArrayList<String>) ObjectSerializer.deserialize(preferences.getString("recentTime",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Title != null && Number != null && Date != null && Time != null && recentAction != null) {

            if (Title.size() == Number.size() && Date.size() == Time.size() && Time.size() == recentAction.size()) {
                mRecentAction.addAll(recentAction);
                mTitle.addAll(Title);
                mNumber.addAll(Number);
                mDate.addAll(Date);
                mTime.addAll(Time);

                // delete array items if it will more than 10.
                if (mRecentAction.size() > 10 && mTitle.size() > 10 && mTime.size() > 10 && mNumber.size() > 10 && mDate.size() > 10){
                    for (int i = 10; i < mRecentAction.size(); i++) {
                        mRecentAction.remove(i);
                        mTitle.remove(i);
                        mNumber.remove(i);
                        mTime.remove(i);
                        mDate.remove(i);
                    }
                }

                // reverse the list
                Collections.reverse(mRecentAction);
                Collections.reverse(mTitle);
                Collections.reverse(mTime);
                Collections.reverse(mNumber);
                Collections.reverse(mDate);

            }
        }


        EmergencyNumbers emergencyNumbers = new EmergencyNumbers();

        if (mTitle != null && mNumber != null)
            if (mTime.size() == mNumber.size() && mTime.size() == mTitle.size() && mTitle.size() == mDate.size())
                for (int i = 0; i < mTitle.size(); i++)
                    number.add(new NumberOfRecentAndHistory(emergencyNumbers.getLattersOfString(mTitle.get(i)), mTitle.get(i), mNumber.get(i),
                            mDate.get(i), mTime.get(i), mRecentAction.get(i)));

        listView = rootView.findViewById(R.id.listView);
        adapter = new NumberAdapterOfRecentAndHistory(getActivity(), number, true);
        listView.setAdapter(adapter);

        return rootView;
    }
}
