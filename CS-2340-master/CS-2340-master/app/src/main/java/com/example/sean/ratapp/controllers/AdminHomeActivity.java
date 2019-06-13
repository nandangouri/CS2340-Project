package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sean.ratapp.R;

/**
 * Created by jfahe on 10/4/2017.
 */

@SuppressWarnings("ALL")
public class AdminHomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);

        Button logOutButton = (Button) findViewById(R.id.logOut);
        Button searchSighting = (Button) findViewById(R.id.searchSighting);
        Button addSighting = (Button) findViewById(R.id.addSighting);
        Button lockuser = findViewById(R.id.lockUsers);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent logoutattempt = new Intent(AdminHomeActivity.this, StartScreenActivity.class);
                startActivity(logoutattempt);
            }
        });

        searchSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(AdminHomeActivity.this, RatSightingListActivity.class);
                startActivity(search);
            }
        });

        addSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(AdminHomeActivity.this, AddSightingActivity.class);
                startActivity(add);
            }
        });

        lockuser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent banattempt = new Intent(AdminHomeActivity.this, BanUserActivity.class);
                startActivity(banattempt);
            }
        });
    }
}
