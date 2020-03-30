package com.proapps.akashsaini.helpme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FirebaseNumberAdapter extends ArrayAdapter<AddPublicNumber> {


    public FirebaseNumberAdapter(AppCompatActivity context, ArrayList<AddPublicNumber> publicNumbers) {
        super(context, 0, publicNumbers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.firebase_number_layout, parent, false);

        TextView name = rootView.findViewById(R.id.name);
        TextView mob1 = rootView.findViewById(R.id.mobile1);
        TextView mob2 = rootView.findViewById(R.id.mobile2);
        TextView state = rootView.findViewById(R.id.stateValue);
        TextView city = rootView.findViewById(R.id.cityValue);
        TextView address1 = rootView.findViewById(R.id.addressValue1);
        TextView address2 = rootView.findViewById(R.id.addressValue2);

        final AddPublicNumber currentNumber = getItem(position);

        assert currentNumber != null;
        name.setText(currentNumber.getmName());
        mob1.setText(currentNumber.getmMob1());
        mob2.setText(currentNumber.getmMob2());
        state.setText(currentNumber.getmState());
        city.setText(currentNumber.getmCity());
        address1.setText(currentNumber.getmAddr1());
        address2.setText(currentNumber.getmAddr2());

        return rootView;
    }
}
