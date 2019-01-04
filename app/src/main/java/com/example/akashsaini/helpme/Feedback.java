package com.example.akashsaini.helpme;

import android.content.Intent;
import android.net.Uri;
import android.opengl.ETC1;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity implements Button.OnClickListener{

    String userEmail;
    String userMessage;
    EditText from;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        from = findViewById(R.id.from);
        message = findViewById(R.id.message);
        Button send = findViewById(R.id.send);

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        userEmail = from.getText().toString();
        userMessage = message.getText().toString();

        if (userEmail.isEmpty())
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        if (userMessage.isEmpty())
            Toast.makeText(this, "Please write your feedback message", Toast.LENGTH_SHORT).show();
        else if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
                && !userMessage.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "vishalsaini16297@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, userMessage + "\nEmail: " + userEmail);
            startActivity(Intent.createChooser(intent, "subject..."));
            finish();

        } else
            Toast.makeText(this, "Email address is incorrect", Toast.LENGTH_SHORT).show();


    }

}
