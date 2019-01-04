package com.example.akashsaini.helpme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class setting extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intent = new Intent(setting.this, MainActivity.class);
        TextView cleanHistory = findViewById(R.id.cleanHistory);
        TextView cleanQuick = findViewById(R.id.cleanQuickTouch);
        TextView cleanRecent = findViewById(R.id.cleanRecent);
        TextView cleanAll = findViewById(R.id.cleanAll);

        preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        cleanHistory.setOnClickListener(this);
        cleanQuick.setOnClickListener(this);
        cleanRecent.setOnClickListener(this);
        cleanAll.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {

        final SharedPreferences.Editor editor = preferences.edit();

        new AlertDialog.Builder(this)

                .setTitle("Delete")
                .setMessage("Do you want to delete")
                .setIcon(R.drawable.ic_delete_listview_item)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (v.getId()) {

                            case R.id.cleanHistory:

                                editor.remove("historyDate").apply();
                                editor.remove("historyTime").apply();
                                editor.remove("historyTitle").apply();
                                editor.remove("historyNumber").apply();
                                Toast.makeText(setting.this, "History Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;

                            case R.id.cleanQuickTouch:

                                editor.remove("quickAccessibleNumbers");
                                editor.remove("quickAccessibleNumberTitles");
                                Toast.makeText(setting.this, "Quick Touch Numbers Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;

                            case R.id.cleanRecent:

                                editor.remove("recentAction").apply();
                                editor.remove("recentTitle").apply();
                                editor.remove("recentNumber").apply();
                                editor.remove("recentDate").apply();
                                editor.remove("recentTime").apply();
                                Toast.makeText(setting.this, "Recent activity Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;

                            case R.id.cleanAll:

                                // clean history
                                editor.remove("historyDate").apply();
                                editor.remove("historyTime").apply();
                                editor.remove("historyTitle").apply();
                                editor.remove("historyNumber").apply();

                                // clean quick access fragment
                                editor.remove("quickAccessibleNumbers");
                                editor.remove("quickAccessibleNumberTitles");

                                // clean recent activity
                                editor.remove("recentAction").apply();
                                editor.remove("recentTitle").apply();
                                editor.remove("recentNumber").apply();
                                editor.remove("recentDate").apply();
                                editor.remove("recentTime").apply();
                                Toast.makeText(setting.this, "All Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
