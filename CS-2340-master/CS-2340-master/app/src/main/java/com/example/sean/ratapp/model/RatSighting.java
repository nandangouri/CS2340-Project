package com.example.sean.ratapp.model;


import android.annotation.SuppressLint;
import android.util.Log;
import java.io.PrintWriter;
import java.util.Random;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Created by jfahe on 10/6/2017.
 */

@SuppressWarnings("ALL")
public class RatSighting {
    private final String locationType;
    private final int key;
    private final int zip;
    private final String address;
    private final String city;
    private final double latitude;
    private final double longitude;
    private final int year;
    private final int month;
    private final String createdDate;
    private final String borough;


    public RatSighting(int key, String createdDate, String locationType, int zip,
                       String address, String city, String borough, double latitude, double longitude) {
        this.locationType = locationType;
        this.key = key;
        this.zip = zip;
        this.address = address;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdDate = createdDate;
        this.borough = borough;
        this.year =  Integer.parseInt(createdDate.substring(6,10));
        this.month =  Integer.parseInt(createdDate.substring(0,2));
    }

    //constructor used for adding rat sightings where uniqueId creates id
    public RatSighting(String createdDate, String locationType, int zip, String address,
                       String city, String borough, double latitude, double longitude) {
        this(uniqueId(), createdDate, locationType,zip, address, city, borough, latitude,
                longitude);
    }

    public int getKey() { return key; }
    public int getMonth() { return month; }
    public String getDate() { return createdDate; }
    public String getLocation() { return locationType; }
    public int getZip() { return zip; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getBorough() { return borough; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    /**
     * method used to generate random id number
     * @return rand id number
     */
    private static int uniqueId() {
        Random rand = new Random();
        int id = rand.nextInt(100);
        id = id * rand.nextInt(50);
        id += rand.nextInt(100);
        return id;
    }
    // toString method to return some info as a string to be displayed in each entry in the ListView
    // that lists all rat sightings. Currently returns unique key of rat sighting.
    @Override
    public String toString() {
        return String.valueOf(key) + " " + this.getCity();
    }

    /**
     * used to write the rat sighting object into the line to be saved
     * @param writer the object writing the rat sighting object
     */
    public void saveAsText(PrintWriter writer) {
        System.out.println("Student saving student: " + key);
        writer.println(key + "\t" + createdDate + "\t" + locationType + "\t" + zip + "\t" + address
        + "\t" + city + "\t" + borough + "\t" + latitude + "\t" + longitude);
    }

    public int getYear() {
        return year;
    }

    /**
     * used to create a new rat sighting object by reading a text line
     * @param line line entered to be read in
     * @return rat sighting created from reading line
     */
    @SuppressLint("Assert")
    public static RatSighting parseEntry(String line) {
        if (line == null) return null;
        if (line == "") return null;
        String[] tokens = line.split("\t");
        assert tokens.length == 9;
        try {
            return new RatSighting(parseInt(tokens[0]), tokens[1], tokens[2],
                    parseInt(tokens[3]), tokens[4], tokens[5], tokens[6], parseDouble(tokens[7]),
                    parseDouble(tokens[8]));
        } catch (Exception e) {
            //Log.e("Error", "Bad sighting save");
            return null;
        }
    }


}
