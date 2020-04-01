package com.proapps.akashsaini.helpme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView textView;
    private ImageView closeButtonImageView;

    private SQLiteDatabase mSQLiteDatabase = null;

    private ListView mNumberListView;
    private TextView mErrorTextView;

    // Instance of NetworkInfo and NetworkManager to check weather if internet i connected or not.
    NetworkInfo networkInfo;
    ConnectivityManager conMgr;

    private ArrayList<AddPublicNumber> publicNumber;
    private FirebaseNumberAdapter fragmentNumberAdapter;

    private static final String TAG = SearchActivity.class.getSimpleName();

    private ChildEventListener mChildEventListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.searchTextView);
        mNumberListView = findViewById(R.id.listView);
        mErrorTextView = findViewById(R.id.errorTextView);
        closeButtonImageView = findViewById(R.id.closeButtonImageView);

        mNumberListView.setEmptyView(mErrorTextView);

        setSupportActionBar(toolbar);

        // Initialize message ListView and its adapter
        publicNumber = new ArrayList<>();
        fragmentNumberAdapter = new FirebaseNumberAdapter(this, publicNumber);
        mNumberListView.setAdapter(fragmentNumberAdapter);

        // Get a reference to the connectivity manager to check the state of network connectivity
        conMgr = (ConnectivityManager) Objects.requireNonNull(this).getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean noNetwork = checkNetworkConnection();
        if (!noNetwork){
            mErrorTextView.setText(R.string.connect_net_for_more_result);
        }

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                publicNumber.clear();
                if (textView.getText().toString().isEmpty()) {
                    closeButtonImageView.setVisibility(View.GONE);
                    publicNumber.clear();
                    fragmentNumberAdapter.notifyDataSetChanged();
                } else {
                    closeButtonImageView.setVisibility(View.VISIBLE);

                    boolean noNetwork = checkNetworkConnection();
                    String editTextData = textView.getText().toString();
                    if (!noNetwork){
                        searchDataOffline();
                        ArrayList<AddPublicNumber> numbers = new ArrayList<>();
                        for (AddPublicNumber number: publicNumber) {
                            if (AddPublicNumber.compareString(editTextData, number)) {
                                numbers.add(number);
                            }
                        }
                        fragmentNumberAdapter = new FirebaseNumberAdapter(SearchActivity.this, numbers);
                        mNumberListView.setAdapter(fragmentNumberAdapter);
                    } else{
                        attachDatabaseReadListener();
                        ArrayList<AddPublicNumber> numbers = new ArrayList<>();
                        for (AddPublicNumber number: publicNumber) {
                            if (AddPublicNumber.compareString(editTextData, number)) {
                                numbers.add(number);
                            }
                        }
                        fragmentNumberAdapter = new FirebaseNumberAdapter(SearchActivity.this, numbers);
                        mNumberListView.setAdapter(fragmentNumberAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkNetworkConnection() {

        // Get details on the currently active default data network
        assert conMgr != null;
        networkInfo = conMgr.getActiveNetworkInfo();

        // If there is not network connection, fetch data
        return networkInfo != null && networkInfo.isConnected();
    }

    private void searchDataOffline() {
        mSQLiteDatabase = Objects.requireNonNull(this).openOrCreateDatabase("COVID_19_HLN", MODE_PRIVATE, null);
        String countQuery = "SELECT * FROM public_numbers";
        Cursor cursor = mSQLiteDatabase.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String uid = cursor.getString(cursor.getColumnIndex("user_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mobile1 = cursor.getString(cursor.getColumnIndex("mobile1"));
                String mobile2 = cursor.getString(cursor.getColumnIndex("mobile2"));
                String state = cursor.getString(cursor.getColumnIndex("state"));
                String pin = cursor.getString(cursor.getColumnIndex("pin"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String address1 = cursor.getString(cursor.getColumnIndex("address1"));
                String address2 = cursor.getString(cursor.getColumnIndex("address2"));

                publicNumber.add(new AddPublicNumber(uid, name, mobile1, mobile2, state, pin, city, address1, address2));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    publicNumber.clear();
                    Log.i(TAG, "after clear publicNumber data");
                    for (DataSnapshot snaps: dataSnapshot.getChildren()){
                        publicNumber.add(snaps.getValue(AddPublicNumber.class));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {            }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {            }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {            }
            };
            mUserReference.addChildEventListener(mChildEventListener);
        }
    }

    private void dettachDatabaseReadListener(){
        if (mChildEventListener != null){
            mUserReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dettachDatabaseReadListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentNumberAdapter.clear();
        Log.i(TAG, "before attach method");
        attachDatabaseReadListener();
    }

    public void backButton(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void closeButton(View view){
        publicNumber.clear();
        fragmentNumberAdapter.notifyDataSetChanged();
        textView.setText("");
    }
}
