package com.example.sean.ratapp.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Sean on 10/9/2017.
 */

@SuppressWarnings("ALL")
public class RatDataReader extends AsyncTask<InputStream, Integer, Long> {

    private static final ArrayList<RatSighting> ratData = new ArrayList<RatSighting>();
    private static final ArrayList<String> ratDataString = new ArrayList<String>();

    // Loads rat sighting data from file and formats into various data structures to return later
    // TODO: right now about three data structures are created from the data in the file, using up
    // a lot of memory; limit the number of data structures created in this method so that memory
    // isn't wasted.
    public void LoadRatData(InputStream ins, int maxEntriesToRead) {

        int i = 0;
        int totalParseCount = 0;
        ArrayList<RatSighting> ratSightList = new ArrayList<RatSighting>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));

            String line;
            br.readLine(); //get rid of header line
            while ((line = br.readLine()) != null && maxEntriesToRead-- > 0) {
                String[] tokens = line.split(",");
                //Error at key 31473349, entry 75  in CSV

                if (tokens.length == 53) {
                    ratSightList.add(new RatSighting(Integer.parseInt(tokens[0]), tokens[1], tokens[7],
                            Integer.parseInt(tokens[8]), tokens[9], tokens[16], tokens[23],
                            Double.parseDouble(tokens[49]), Double.parseDouble(tokens[50])));
                }
                totalParseCount++;
            }
            br.close();
        } catch (IOException e) {
            Log.e("Tag", "error reading assets", e);
        }

        for (RatSighting r : ratSightList) {
            i++;
            ratData.add(r);
            ratDataString.add(r.getKey() + " " + r.getCity());
        }
        System.out.println(i + " entries parsed correctly out of " + totalParseCount);
    }

    /**
     * getter for rat data array
     * @return the rat data array
     */
    public static ArrayList<RatSighting> getRatDataArray() {
        return ratData;
    }

    /**
     * getter for rat data string array
     * @return array of rat data strings
     */
    public ArrayList<String> getRatDataString() { return ratDataString; }

    /**
     * adds a new rat sighting to the rat data array
     */
    public void addSighting(RatSighting sighting) {
        ratData.add(0, sighting);
        ratDataString.add(0, sighting.toString());


    }

    @Override
    protected Long doInBackground(InputStream... params) {

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {

    }

    /**
     * saves rat data by calling ratsighting saveAsText method
     * @param writer object used to write to the text file
     */
    void saveAsText(PrintWriter writer) {
        System.out.println("Manager saving: " + ratData.size() + " rat sightings" );
        writer.println(ratData.size());
        for(RatSighting s : ratData) {
            s.saveAsText(writer);
        }
    }

    /**
     * loads rat text by creating new rat sightings from the text file and adds them back to the
     * arraylist
     * also recreates the string array list
     * @param reader the object that reads the file data
     */
    void loadFromText(BufferedReader reader) {
        ratData.clear();
        ratDataString.clear();
        try {
            String countStr = reader.readLine();
            assert countStr != null;

            try {
                int count = Integer.parseInt(countStr);

                //then read in each user to model
                for (int i = 0; i < count; ++i) {
                    String line = reader.readLine();
                    RatSighting s = RatSighting.parseEntry(line);
                    if (s != null) {
                        ratData.add(s);
                        ratDataString.add(s.toString());
                    }
                }
            } catch (Exception nfe) {
                String curLine = reader.readLine();
                while (curLine != null) {

                    String line = reader.readLine();
                    RatSighting s = RatSighting.parseEntry(line);
                    if (s != null) {
                        ratData.add(s);
                        ratDataString.add(s.toString());
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done loading text file with " + ratData.size() + " rat sightings");
    }
}
