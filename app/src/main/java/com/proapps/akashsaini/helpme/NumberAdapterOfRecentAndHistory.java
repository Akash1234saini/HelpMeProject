package com.proapps.akashsaini.helpme;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NumberAdapterOfRecentAndHistory extends ArrayAdapter<NumberOfRecentAndHistory> {

    private boolean isRecentActivity;

    public NumberAdapterOfRecentAndHistory(Activity context, ArrayList<NumberOfRecentAndHistory> list, boolean recentActivity) {
        super(context, 0, list);
        isRecentActivity = recentActivity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.history_number_layout, parent, false);

        TextView latters = rootView.findViewById(R.id.numberIconHistory);
        TextView title = rootView.findViewById(R.id.numberTitleHistory);
        TextView number = rootView.findViewById(R.id.numberHistory);
        TextView date = rootView.findViewById(R.id.callDate);
        TextView time = rootView.findViewById(R.id.callTime);

        TextView recentActivity = rootView.findViewById(R.id.recentActivity);

        // instance
        NumberOfRecentAndHistory currentActivity = getItem(position);

        assert currentActivity != null;
        latters.setText(currentActivity.getmTitleLatters());
        title.setText(currentActivity.getmTitle());
        number.setText(currentActivity.getmNumber());
        date.setText(currentActivity.getmDate());
        time.setText(currentActivity.getmTime());

        if (recentActivity != null && isRecentActivity) {
            recentActivity.setVisibility(View.VISIBLE);
            recentActivity.setText(currentActivity.getRecentActivity());
        } else if (recentActivity != null){
            recentActivity.setVisibility(View.GONE);
        }

        return rootView;
    }
}
