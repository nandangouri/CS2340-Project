package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.Model;
import com.example.sean.ratapp.model.User;
import com.example.sean.ratapp.model.UserManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by jfahe on 9/29/2017.
 */

@SuppressWarnings("ALL")
public class RegisterActivity extends AppCompatActivity {
    private EditText _userName;
    private EditText _password;
    private String _user_name;
    private String _pass_word;
    private CheckBox _admin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button _register = (Button) findViewById(R.id.registerBtn);
        Button _back = (Button) findViewById(R.id.cancel);
        _userName = (EditText) findViewById(R.id.userNameText);
        _password = (EditText) findViewById(R.id.passwordText);
        _admin = (CheckBox) findViewById(R.id.admin);

        _register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _user_name = _userName.getText().toString();
                _pass_word = _password.getText().toString();

                if (_admin.isChecked()) {
                    if (UserManager.addAdmin(_user_name, _pass_word)) {
                        // A new admin has been registered
                        saveUserText(UserManager._admin_hash_map);
                        uploadUsersToFirebase();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, AdminHomeActivity.class));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Username " +
                                        "please enter new Username",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,-400);
                        toast.show();
                    }
                } else {
                    if (UserManager.addUser(_user_name, _pass_word)) {
                        // A new user has been registered
                        saveUserText(UserManager._user_hash_map);
                        uploadUsersToFirebase();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Username " +
                                "please enter new Username",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,-400);
                        toast.show();
                    }
                }
            }
        });

        _back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this, StartScreenActivity.class));
            }
        });

    }

    /**
     * saves rat data to a txt file
     * @param allUsers the hashmap containing all registered users, stored in UserManager.
     * @return returns true if file save was successful false if file cant be found
     */
    private void saveUserText(HashMap<String, User> allUsers) {

        File file = new File(getFilesDir(), Model.DEFAULT_USERTEXT_FILE_NAME);

        System.out.println("Saving users as a text file");
        try {
            PrintWriter pw = new PrintWriter(file);


            System.out.println("RegisterActvity saving users" );
            pw.println(allUsers.size());


            JSONObject usersObject = new JSONObject();

            for(String key : allUsers.keySet()) {
                try {
                    User user = allUsers.get(key);
                    usersObject.put(key, user.getPassword());
                } catch (JSONException e) {
                    System.out.println("Error saving user into JSON object");
                }
            }
            pw.write((usersObject.toString()));

            System.out.println(usersObject.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("model", "Error opening the text file for save!");
        }

    }

    /**
     * @param file the file containing all the JSON representatives of Users
     * @return true if loading was successful
     */
    public static void loadUsersFromJSON(File file) {

        try {
            //make an input object for reading
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
                String jsonTxt = reader.readLine();
                jsonTxt = reader.readLine();
                System.out.println("File Contents: " + jsonTxt);
                try {
                    JSONObject json = new JSONObject(jsonTxt);
                    System.out.println("JSON Contents: " + json.toString());
                    Iterator<?> keys = json.keys();
                    int i = 0;
                    int limit = 20;
                    while (keys.hasNext()/* && i < limit*/){
                        String username = (String)keys.next();
                        String pass = json.getString(username);
                        UserManager._user_hash_map.put(username, new User(username, pass));

                        System.out.println("Key get " + username + " " + i++);
                        //json.keys().remove();
                    }
                    if (i >= limit) {
                        System.out.println("Parsed to limit");
                    }
                } catch (JSONException e) {
                    System.out.println("Error loading from JSON");
                }
            } catch (Exception e) {

                System.out.println("Error loading from file");
            }

        } catch (FileNotFoundException e) {
            Log.e("ModelSingleton", "Failed to open text file for loading!");
        }

    }


    private void uploadUsersToFirebase() {

        System.out.println("Starting upload to Firebase...");

        Uri file = Uri.fromFile(new File(getFilesDir(), Model.DEFAULT_USERTEXT_FILE_NAME));

        System.out.println("File URI: " + file.toString());
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rat-app-22f3a.appspot.com/user.txt");

            System.out.println("@@@@@ Point A @@@@@");
            storageReference.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            System.out.println("Uploading to Firebase sucessfull!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...

                            System.out.println("Uploading to Firebase failed...");
                        }
                    });
        } catch (Exception e) {
            System.out.println("Caught Storage Exception!");
        }

    }
}
