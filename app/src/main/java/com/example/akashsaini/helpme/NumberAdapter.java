package com.example.akashsaini.helpme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Allocation;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NumberAdapter extends ArrayAdapter<Number> {

    private Context mContext;
    private boolean mShowAlphabets;
    private boolean mShowStar;

    NumberAdapter(Activity context, ArrayList<Number> numbers, boolean showAlphabets, boolean showStar) {
        super(context, 0 , numbers);
        mContext  = context;
        mShowAlphabets = showAlphabets;
        mShowStar = showStar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.number_layout, parent, false);

        TextView numberTitle = rootView.findViewById(R.id.numberTitle);
        TextView number = rootView.findViewById(R.id.number);
        TextView textIcon = rootView.findViewById(R.id.numberIcon);
        TextView alphabet = rootView.findViewById(R.id.alphabet_list);
        TextView orderToStart = rootView.findViewById(R.id.orderToStart);
        ImageView star = rootView.findViewById(R.id.myFavourite);

        final Number currentNumber = getItem(position);

        assert currentNumber != null;

        numberTitle.setText(currentNumber.getmNumberTitle());
        number.setText(currentNumber.getmNumber());
        textIcon.setText(currentNumber.getmNumberIcon());

        alphabet.setVisibility(View.GONE);
        orderToStart.setVisibility(View.GONE);
        star.setVisibility(View.GONE);

        if (mShowStar)
            star.setVisibility(View.VISIBLE);

        if (mShowAlphabets) {

            for (int i = 65; i < 91; i++)
                if (currentNumber.getmNumberTitle().startsWith(String.valueOf((char) i))) {
                    alphabet.setVisibility(View.VISIBLE);
                    alphabet.setText(String.valueOf((char) i));
                    break;
                } else {
                    alphabet.setVisibility(View.GONE);
                }

            if (position > 0 && currentNumber.getmNumberTitle().startsWith(
                    String.valueOf(EmergencyNumbers.number.get(position - 1).getmNumberTitle().charAt(0)))) {
                alphabet.setVisibility(View.GONE);
            }

            // getting indication that order of list item changed
            if (alphabet.getVisibility() == View.VISIBLE) {
                orderToStart.setVisibility(View.VISIBLE);
                orderToStart.setText(alphabet.getText());
            } else {
                orderToStart.setVisibility(View.GONE);
            }
        }
        return rootView;
    }
}
