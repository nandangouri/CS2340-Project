package com.example.sean.ratapp.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.Model;
import com.example.sean.ratapp.model.RatDataReader;
import com.example.sean.ratapp.model.RatSighting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by jfahe on 10/13/2017.
 */

@SuppressWarnings("ALL")
public class AddSightingActivity extends AppCompatActivity {
    private EditText address;
    private EditText city;
    private EditText locationType;
    private EditText zip;
    private EditText borough;
    private EditText date;
    private EditText latitude;
    private EditText longitude;
    private RatDataReader rdr;
    private final Model model = Model.INSTANCE;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sighting);

        // setting up edit texts to corresponding variables
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        locationType = (EditText) findViewById(R.id.locationType);
        zip = (EditText) findViewById(R.id.zipcode);
        borough = (EditText) findViewById(R.id.borough);
        date = (EditText) findViewById(R.id.date);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);

        Button addSighting = (Button) findViewById(R.id.add);

        rdr = new RatDataReader();

        /*when add sighting button is clicked we create new new sighting object then add it to the
        ratsightings array list then it takes you to the activity with all the sightings in a
        list*/
        addSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(zip)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid ZipCode",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (isValid(latitude)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Latitude",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (isValid(longitude)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Longitude",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    RatSighting sighting = new RatSighting(date.getText().toString(),
                            locationType.getText().toString(),
                            Integer.parseInt(zip.getText().toString()),
                            address.getText().toString(), city.getText().toString(),
                            borough.getText().toString(),
                            Double.parseDouble(latitude.getText().toString()),
                            Double.parseDouble(longitude.getText().toString()));

                    rdr.addSighting(sighting);
                    uploadSightingsToFirebase();

                    file = new File(getFilesDir(), Model.DEFAULT_RATTEXT_FILE_NAME);
                    model.saveRatText(file);
                    finish();
                    Intent go = new Intent(AddSightingActivity.this, RatSightingListActivity.class);
                    startActivity(go);
                }
            }
        });
    }

    private static boolean isValid(EditText text) {
        return text.getText().toString().equals("");
    }


    private void uploadSightingsToFirebase() {

        System.out.println("Starting rat sightings upload to Firebase...");

        Uri file = Uri.fromFile(new File(getFilesDir(), Model.DEFAULT_RATTEXT_FILE_NAME));

        System.out.println("File URI: " + file.toString());
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rat-app-22f3a.appspot.com/ratdata.txt");

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

                            System.out.println("[rat sightings] Uploading to Firebase failed...");
                        }
                    });
        } catch (Exception e) {
            System.out.println("Caught Storage Exception!");
        }

    }
}