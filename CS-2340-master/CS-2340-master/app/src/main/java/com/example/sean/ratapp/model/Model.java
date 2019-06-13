package com.example.sean.ratapp.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Created by jfahe on 10/6/2017.
 */

@SuppressWarnings("ALL")
public class Model {
    public static final Model INSTANCE = new Model();

    private final RatDataReader rdr = new RatDataReader();

    public final static String DEFAULT_RATTEXT_FILE_NAME = "ratdata.txt"; //used for rat data
    public final static String DEFAULT_USERTEXT_FILE_NAME = "user.txt";

    /**
     * saves rat data to a txt file
     * @param file the name of the txt file saved
     * @return returns true if file save was successful false if file cant be found
     */
    public void saveRatText(File file) {
        System.out.println("Saving as a text file");
        try {
            PrintWriter pw = new PrintWriter(file);
            rdr.saveAsText(pw);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("model", "Error opening the text file for save!");
        }

    }

    /**
     * loads rat data
     * @param file file name of loaded rat data
     * @return true if successfull false if file not found
     */
    public void loadRatText(File file) {
        try {
            //make an input object for reading
            BufferedReader reader = new BufferedReader(new FileReader(file));
            rdr.loadFromText(reader);

        } catch (FileNotFoundException e) {
            Log.e("ModelSingleton", "Failed to open text file for loading!");
        }
    }

}
