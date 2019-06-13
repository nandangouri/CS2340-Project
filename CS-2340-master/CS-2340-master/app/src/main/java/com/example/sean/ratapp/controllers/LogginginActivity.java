package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.Locked;
import com.example.sean.ratapp.model.User;
import com.example.sean.ratapp.model.UserManager;

/**
 * Created by jfahe on 9/22/2017.
 */

@SuppressWarnings("ALL")
public class LogginginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    // username and password stored in a String, since they are used in multiple places in the code
    private String _user_name;
    private String _pass_word;
    private int _attempts = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggingin);

        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);

        Button logIn = (Button) findViewById(R.id.enter_login);
        Button back = (Button) findViewById(R.id.back);

        if (Locked.getLocked() == true) {
            logIn.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "You have been locked out, please try again later.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,-300);
            toast.show();
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _pass_word = password.getText().toString();
                _user_name = userName.getText().toString();

                // if the user has registered and is in the hash map, check login information.
                if (UserManager.loginAdmin(_user_name, _pass_word)) {
                    _attempts = 0;
                    finish();
                    startActivity(new Intent(LogginginActivity.this, AdminHomeActivity.class));

                } else if (UserManager.loginUser(_user_name, _pass_word)) {

                    User temp = UserManager.getUser(_user_name);

                    if (!temp.isActive()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Account Has Been Banned Due To " + temp.getBanStatus(),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, -300);
                        toast.show();
                    } else if (temp.isActive()) {
                            _attempts = 0;
                            finish();
                            startActivity(new Intent(LogginginActivity.this, HomeActivity.class));
                        }
                } else {
                    _attempts++;
                    if (_attempts > 2){
                        Locked.lock();
                        Toast toast = Toast.makeText(getApplicationContext(), "Too many invalid attempts, you have been locked out.",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,-300);
                        toast.show();
                        logIn.setVisibility(View.GONE);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid User information",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, -300);
                        toast.show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LogginginActivity.this, StartScreenActivity.class));
            }
        });
    }
}
