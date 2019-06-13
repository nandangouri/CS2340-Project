package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.User;
import com.example.sean.ratapp.model.UserManager;

/**
 * Created by chuwh on 12/3/2017.
 */

public class BanUserActivity extends AppCompatActivity {
    String username;
    String reason;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_user);

        Button banButton = findViewById(R.id.banbutton);
        EditText userToBan = findViewById(R.id.userToBan);
        Spinner reasonToBan = findViewById(R.id.reasonSpinner);

        banButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userToBan.getText().toString();
                reason = String.valueOf(reasonToBan.getSelectedItem());

                if (username == null) {
                    throw new IllegalArgumentException("username cannot be null");
                }

                if (reason == null) {
                    throw new IllegalArgumentException("explanation cannot be null");
                }

                if (UserManager._user_hash_map.containsKey(username)) {
                    User temp = User.getUserByID(username);
                    temp.setActive(false);
                    temp.setBanStatus(reason);

                    Toast.makeText(getApplicationContext(), "User " + username + " has been banned successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Ban Was UnSuccessful. Retry Username.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
