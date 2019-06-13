package com.example.sean.ratapp.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.RatDataReader;
import com.example.sean.ratapp.model.RatSighting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static void SetDateRange(String startDateStringInput, String endDateStringInput) {
        startDateString = startDateStringInput;
        endDateString = endDateStringInput;
    }

    private static String startDateString;
    private static String endDateString;

    @Override
    public void onMapReady(GoogleMap googleMap) {


        ArrayList<RatSighting> ratDataArray = RatDataReader.getRatDataArray();



        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate;
        try{
            startDate = sdf.parse(startDateString);
        } catch (ParseException pe) {
            System.out.println("Parsing start date (" + startDateString + ") failed.");
            startDate = Calendar.getInstance().getTime();
        }
        Date endDate;
        try{
            endDate = sdf.parse(endDateString);
        } catch (ParseException pe) {
            System.out.println("Parsing start date (" + startDateString + ") failed.");
            endDate = Calendar.getInstance().getTime();
        }

        // Find the first rat entry past the start date
        boolean greaterDateFound = false;
        int start = 0;
        int i;
        String testDateString;
        for (i = 0; i < ratDataArray.size() && !greaterDateFound; i++) {
            testDateString = ratDataArray.get(i).getDate();
            Date testDate;
            try {
                testDate = sdf.parse(testDateString.substring(0, 10));
            } catch (ParseException e) {
                System.out.println("Parsing test date (" + testDateString + ") failed.");
                testDate = Calendar.getInstance().getTime();
            }
            if (testDate.after(startDate)) {
                System.out.println("Start date found: " + testDateString + " after " + start + " parses");
                greaterDateFound = true;
            }
            start++;
        }
        if (!greaterDateFound) {
            System.out.println("No end date found after " + start + " parses.");
        }

        // Find the first rat entry before the end date
        greaterDateFound = false;
        int end = start;
        String testEndDateString;
        for (; i < ratDataArray.size() && !greaterDateFound; i++) {
            testEndDateString = ratDataArray.get(i).getDate();
            Date testDate;
            try {
                testDate = sdf.parse(testEndDateString.substring(0, 10));
            } catch (ParseException e) {
                System.out.println("Parsing test date (" + testEndDateString + ") failed.");
                testDate = Calendar.getInstance().getTime();
            }
            if (testDate.after(endDate)) {
                System.out.println("End date found: " + testEndDateString + " after " + end + " parses");
                greaterDateFound = true;
            }
            end++;
        }
        if (!greaterDateFound) {
            System.out.println("No end date found after " + end + " parses.");
        }

        ArrayList<RatSighting> badSolution = new ArrayList<>();

        for (int z = 0; z < ratDataArray.size(); z++) {
            testEndDateString = ratDataArray.get(z).getDate();
            Date testDate;
            try {
                testDate = sdf.parse(testEndDateString.substring(0, 10));
            } catch (ParseException e) {
                System.out.println("Parsing test date (" + testEndDateString + ") failed.");
                testDate = Calendar.getInstance().getTime();
            }
            if (testDate.after(startDate) && testDate.before(endDate)) {
                badSolution.add(ratDataArray.get(z));
            }
        }

        // Add markers for each ratSighting in the date range
        for (int y =0; y < badSolution.size(); y++) {
            RatSighting sighting = badSolution.get(y);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(sighting.getLatitude(),
                    sighting.getLongitude())).title("Sighting ID: " + sighting.getKey()));

        }

        // Add a marker in New York, and move the camera.
        LatLng ny = new LatLng(40, -74);
        googleMap.addMarker(new MarkerOptions().position(ny).title("Marker in New York"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
