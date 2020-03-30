package com.proapps.akashsaini.helpme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;

public class AddPublicHelplineNumberActivity extends AppCompatActivity {

    private EditText mNameEditText, mMobile1EditText, mMobile2EditText, mStateEditText, mPinEditText, mCityEditText, mAddressLine1EditText, mAddressLine2EditText;
    private static final String TAG = AddPublicHelplineNumberActivity.class.getSimpleName();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;
    private DatabaseReference mUserDetailReference;
    private FirebaseUser mUser;

    public static final int RC_SIGN_IN = 1;
    
    private String mAddress;
    private String mCity;
    private String mState;
    private String mMobile1;
    private String mName;

    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference userDetailRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_public_helpline_number);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mMobile1EditText = (EditText) findViewById(R.id.phone1EditText);
        mMobile2EditText = (EditText) findViewById(R.id.phone2EditText);
        mStateEditText = (EditText) findViewById(R.id.stateEditText);
        mPinEditText = (EditText) findViewById(R.id.pinEditText);
        mCityEditText = (EditText) findViewById(R.id.cityEditText);
        mAddressLine1EditText = (EditText) findViewById(R.id.addressLine1EditText);
        mAddressLine2EditText = (EditText) findViewById(R.id.addressLine2EditText);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference("users");
        mUserDetailReference = mFirebaseDatabase.getReference();
        mUser = mFirebaseAuth.getCurrentUser();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    // user is sighed in
                    fillUserDataInEditors();
                } else {
                    // user is signed out

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


        userDetailRef = mFirebaseDatabase.getReference("users");
    }

    private void fillUserDataInEditors() {
        attachDatabaseReadListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_public_number_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:

                boolean isAllRight = checkMandatoryFields();
                if (isAllRight){
                    uploadingTask();
                }
                break;
            case R.id.action_delete:
                deletingTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletingTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure")
                .setMessage("You want to delete your number permanently!\n(You can add number later)")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(AddPublicHelplineNumberActivity.this, "Deleting...", Toast.LENGTH_SHORT).show();
                        deleteNumber();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void uploadingTask() {
        String mobile2 = mMobile2EditText.getText().toString();
        String pin = mPinEditText.getText().toString();
        String mUserUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String address2 = mAddressLine2EditText.getText().toString();
        AddPublicNumber publicNumber = new AddPublicNumber(mUserUID, mName, mMobile1, mobile2, mState, pin, mCity, mAddress, address2);
        mUserReference.child(mUser.getUid()).setValue(publicNumber)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(AddPublicHelplineNumberActivity.this, "You'are ready to help", Toast.LENGTH_SHORT).show();
                            launchActivity();
                        } else{
                            Toast.makeText(AddPublicHelplineNumberActivity.this, "Something wants wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteNumber() {

        userDetailRef.child(mUser.getUid()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(AddPublicHelplineNumberActivity.this, "Your number deleted permanently", Toast.LENGTH_SHORT).show();
                            launchActivity();
                        } else{
                            Toast.makeText(AddPublicHelplineNumberActivity.this, "Something wants wrong! Try Later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void launchActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed In Cancelled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AddPublicNumber publicNumber = dataSnapshot.child(mUser.getUid()).getValue(AddPublicNumber.class);
                    assert publicNumber != null;
                    mNameEditText.setText(publicNumber.getmName());
                    mMobile1EditText.setText(publicNumber.getmMob1());
                    mMobile2EditText.setText(publicNumber.getmMob2());
                    mStateEditText.setText(publicNumber.getmState());
                    mPinEditText.setText(publicNumber.getmPin());
                    mCityEditText.setText(publicNumber.getmCity());
                    mAddressLine1EditText.setText(publicNumber.getmAddr1());
                    mAddressLine2EditText.setText(publicNumber.getmAddr2());
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
            mUserDetailReference.addChildEventListener(mChildEventListener);
        }
    }

    private void dettachDatabaseReadListener(){
        if (mChildEventListener != null){
            mUserDetailReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private boolean checkMandatoryFields() {

        return checkNameValidation()
                & checkMobile1Validation()
                & checkStateValidation()
                & checkCityValidation()
                & checkAddress1Validation();
    }

    private boolean checkAddress1Validation() {
        mAddress = mAddressLine1EditText.getText().toString();
        if (mAddress.isEmpty()){
            mAddressLine1EditText.setError("Field is empty");
            return false;
        } else{
            mAddressLine1EditText.setError(null);
            return true;
        }
    }

    private boolean checkCityValidation() {
        mCity = mCityEditText.getText().toString();
        if (mCity.isEmpty()){
            mCityEditText.setError("Field is empty");
            return false;
        } else{
            mCityEditText.setError(null);
            return true;
        }
    }

    private boolean checkStateValidation() {
        mState = mStateEditText.getText().toString();
        if (mState.isEmpty()){
            mStateEditText.setError("Field is empty");
            return false;
        } else{
            mStateEditText.setError(null);
            return true;
        }
    }

    private boolean checkMobile1Validation() {
        mMobile1 = mMobile1EditText.getText().toString();
        if (mMobile1.isEmpty()){
            mMobile1EditText.setError("Field is empty");
            return false;
        } else{
            mMobile1EditText.setError(null);
            return true;
        }
    }

    private boolean checkNameValidation() {
        mName = mNameEditText.getText().toString();
        if (mName.isEmpty()){
            mNameEditText.setError("Field is empty");
            return false;
        } else{
            mNameEditText.setError(null);
            return true;
        }
    }
}
