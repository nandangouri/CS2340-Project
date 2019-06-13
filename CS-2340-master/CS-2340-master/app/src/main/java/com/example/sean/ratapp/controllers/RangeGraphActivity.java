package com.example.sean.ratapp.controllers;

import android.app.DatePickerDialog;
//import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//import android.graphics.Color;
//import android.view.Gravity;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
import android.widget.DatePicker;
//import android.widget.Toast;

import com.example.sean.ratapp.R;
import com.example.sean.ratapp.model.RatDataReader;
import com.example.sean.ratapp.model.RatSighting;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
//import java.util.Date;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 11/7/2017.
 */

@SuppressWarnings("ALL")
public class RangeGraphActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private boolean startDateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sighting_range);

        // TODO: program button in layout to bring up another DatePickerDialog to specify an end
        // date for the range
        final DatePickerDialog dpdStart = new DatePickerDialog(this, this, 2015, 0, 1);
        dpdStart.show();
        final DatePickerDialog dpdEnd = new DatePickerDialog(this, this, 2015, 0, 1);

        dpdEnd.show();

        RatDataReader rdr = new RatDataReader();
       //load in rat data
        ArrayList<RatSighting> sightings = RatDataReader.getRatDataArray(); // this is correct

        // sort data based on year (as an int)
        Collections.sort(sightings, new Comparator<RatSighting>() {
                    @Override
                    public int compare(RatSighting r1, RatSighting r2) {
                        return r2.getYear() - r1.getYear();
                    }
                });
        //establish max and min years for graphing purposes
        int minyear = sightings.get(0).getYear();
        int maxyear = sightings.get(sightings.size()-1).getYear();

        System.out.println("Rat data length: " + sightings.size() + " FIrst entry at: " + minyear + " last entry at: " + maxyear);
        Map<Integer, Integer> points = new HashMap<>();

        //adds sightings to hashmap with value of 1. For each recurrence of the year increases the value by 1

        for(RatSighting r: sightings){
            int yearMonth;
            if (r.getMonth() < 10) {
                yearMonth = Integer.parseInt(r.getYear() + "0" + r.getMonth());
            } else {
                yearMonth = Integer.parseInt(r.getYear() + "" + r.getMonth());
            }

            if(points.containsKey(yearMonth)){
                System.out.println("Old point: " + yearMonth);
                points.put(yearMonth, points.get(yearMonth) + 1);
            } else {
                points.put(yearMonth,1);
                System.out.println("New point: " + yearMonth);
            }
        }

        List<DataPoint> dataPoints = new ArrayList<>();

        //cycle through hashmap and create new data point for each KV pair
            //with x coordinate being the key (year) and y coordinate
                //being the value (number of occurrences)
        int numPoints  =0;

        for(Map.Entry<Integer,Integer> p : points.entrySet()){
            DataPoint x = new DataPoint( (p.getKey() / 100), p.getValue());
            dataPoints.add(x);
            numPoints++;
        }
        System.out.println("Num data points added: " + numPoints);
        //convert arraylist to array bc the API will only accept an array
        DataPoint[] pointsarray = dataPoints.toArray(new DataPoint[dataPoints.size()]);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
       // graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
       // graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        LineGraphSeries<DataPoint> pointseries = new LineGraphSeries<>(pointsarray);

        graph.addSeries(pointseries);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date (Year)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Number of Sightings");
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.setTitle("Historical Rat Data");

    }

    // for use with the DatePickerDialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (!startDateSet) {
            startDateSet = true;
        } else {
            startDateSet = false;
        }
    }
}
