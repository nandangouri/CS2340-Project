package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.Model;
import com.example.sean.ratapp.model.RatDataReader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StartScreenActivity extends AppCompatActivity {

    private final Model model = Model.INSTANCE;

    private static StorageReference userDataRef;

    FirebaseAuth mAuth;
    File userFile;
    File ratFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        mAuth = FirebaseAuth.getInstance();
        userDataRef = FirebaseStorage.getInstance().getReference();

        Button logInButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        RatDataReader rdr = new RatDataReader();

        ratFile = new File(this.getFilesDir(), Model.DEFAULT_RATTEXT_FILE_NAME);


        if (userFile == null) {

            userFile = new File(this.getFilesDir(), Model.DEFAULT_USERTEXT_FILE_NAME);
            System.out.println("[users]Downloading from Firebase failed...");
        }

        model.loadRatText(ratFile);
        RegisterActivity.loadUsersFromJSON(userFile);

            // open file that contains rat sighting data
            InputStream ins = getResources().openRawResource(getResources().getIdentifier(
                    "rat_sightings", "raw", getPackageName()));

            // load rat sighting data from file onto device memory
            rdr.LoadRatData(ins, 800);
            // TODO: For some reason outOfBounds errors occur after around 70 lines.

        logInButton.setOnClickListener(v -> {
            finish();
            Intent loginAttempt = new Intent(StartScreenActivity.this, LogginginActivity.class);
            startActivity(loginAttempt);
        });

        registerButton.setOnClickListener(v -> {
            finish();
            Intent registerAttempt = new Intent(StartScreenActivity.this, RegisterActivity.class);
            startActivity(registerAttempt);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            System.out.println("signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Access the user data stored in Firebase Storage.
                            // If it is unsuccessful, it will access the locally stored user data file.
                            userFile = downloadUsersFromFirebase();
                            ratFile = downloadRatSightingsFromFirebase();
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("signInAnonymously:failure");
                            Toast.makeText(StartScreenActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public static StorageReference getUserStorageRef() {
        return userDataRef;
    }

    private File downloadUsersFromFirebase() {
        System.out.println("Starting download from Firebase...");

        userFile = new File(this.getFilesDir(), Model.DEFAULT_USERTEXT_FILE_NAME);

        System.out.println("Download Userfile URI: " + Uri.fromFile(userFile));
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rat-app-22f3a.appspot.com/user.txt");

        storageReference.getFile(userFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                // ...
                System.out.println("Downloading from Firebase successful!");
            }
        }).addOnFailureListener(new OnFailureListener() {@Override
        public void onFailure(@NonNull Exception exception) {
            // Handle failed download
            // ...
            System.out.println("Downloading from Firebase failed!");
        }});
        return userFile;

    }


    private File downloadRatSightingsFromFirebase() {
        System.out.println("Starting rat sighting download from Firebase...");

        ratFile = new File(this.getFilesDir(), Model.DEFAULT_RATTEXT_FILE_NAME);

        System.out.println("Download Userfile URI: " + Uri.fromFile(ratFile));
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rat-app-22f3a.appspot.com/ratdata.txt");

        storageReference.getFile(ratFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                // ...
                System.out.println("Downloading from Firebase successful!");
            }
        }).addOnFailureListener(new OnFailureListener() {@Override
        public void onFailure(@NonNull Exception exception) {
            // Handle failed download
            // ...
            System.out.println("Downloading from Firebase failed!");
        }});
        model.loadRatText(ratFile);
        return ratFile;

    }
}
