package com.example.sean.ratapp;

import com.example.sean.ratapp.model.RatSighting;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Sean on 11/12/2017.
 */

public class ParseEntryTest {
    @Test
    public void testParseNull() {
        assertNull(RatSighting.parseEntry(null));
        assertNull(RatSighting.parseEntry(""));
    }

    @Test
    public void testParseCorrectFormat() {

        RatSighting rs = new RatSighting(31464015, "09/04/2015", "3+ Family Mixed Use Building", 10006, "TRINITY PLACE",
                "MANHATTAN", "MANHATTAN", 40.70777155363643, -74.01296309970473);

        RatSighting testRS = RatSighting.parseEntry("31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM,DOHMH,Department of Health and Mental Hygiene,Rodent,Rat Sighting,3+ Family Mixed Use Building,10006,,,,,TRINITY PLACE,RECTOR STREET,INTERSECTION,NEW YORK,,N/A,Closed,10/04/2015 03:01:02 PM,09/18/2015 12:00:00 AM,01 MANHATTAN,MANHATTAN,980656,197137,Unspecified,MANHATTAN,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,N,,,,,,,,,,,,40.70777155363643,-74.01296309970473,\"(40.70777155363643, -74.01296309970473)\"");


    }

    @Test
    public void testParseIncorrectFormat() {

        // A correctly formatted sighting
        RatSighting rs = new RatSighting(31464015, "09/04/2015", "3+ Family Mixed Use Building", 10006, "TRINITY PLACE",
                "MANHATTAN", "MANHATTAN", 40.70777155363643, -74.01296309970473);

        // an incorrectly formatted rat string is parsed; this one has an additional property in the middle("cat")
        RatSighting testRS = RatSighting.parseEntry("31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM,DOHMH,Department of Health and Mental Hygiene, cat,Rodent,Rat Sighting,3+ Family Mixed Use Building,10006,,,,,TRINITY PLACE,RECTOR STREET,INTERSECTION,NEW YORK,,N/A,Closed,10/04/2015 03:01:02 PM,09/18/2015 12:00:00 AM,01 MANHATTAN,MANHATTAN,980656,197137,Unspecified,MANHATTAN,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,N,,,,,,,,,,,,40.70777155363643,-74.01296309970473,\"(40.70777155363643, -74.01296309970473)\"");

        if (testRS != null) {

            assertTrue(testRS.getDate() == rs.getDate());
            assertTrue(testRS.getKey() == rs.getKey());
            assertTrue(testRS.getAddress() == null);
            assertTrue(testRS.getBorough() == null);
            assertTrue(testRS.getCity() == null);
            assertTrue(testRS.getLocation() == null);
            assertTrue(testRS.getLatitude() == 0);
            assertTrue(testRS.getLongitude() == 0);
        }else {

            assertTrue(false);
        }


        // an incorrectly formatted rat string is parsed; this one has properties in the wrong order from the beginning
        testRS = RatSighting.parseEntry("Department of Health and Mental Hygiene,Rodent,Rat Sighting,31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM,DOHMH,3+ Family Mixed Use Building,10006,,,,,TRINITY PLACE,RECTOR STREET,INTERSECTION,NEW YORK,,N/A,Closed,10/04/2015 03:01:02 PM,09/18/2015 12:00:00 AM,01 MANHATTAN,MANHATTAN,980656,197137,Unspecified,MANHATTAN,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,N,,,,,,,,,,,,40.70777155363643,-74.01296309970473,\"(40.70777155363643, -74.01296309970473)\"");

        if (testRS != null) {

            assertTrue(testRS.getAddress() == null);
            assertTrue(testRS.getBorough() == null);
            assertTrue(testRS.getCity() == null);
            assertTrue(testRS.getDate() == null);
            assertTrue(testRS.getLocation() == null);
            assertTrue(testRS.getKey() == 0);
            assertTrue(testRS.getLatitude() == 0);
            assertTrue(testRS.getLongitude() == 0);
        }else {

            assertTrue(false);
        }

        // an incorrectly formatted rat string is parsed; this one has an additional property on the end ("cat")
        testRS = RatSighting.parseEntry("31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM,DOHMH,Department of Health and Mental Hygiene,Rodent,Rat Sighting,3+ Family Mixed Use Building,10006,,,,,TRINITY PLACE,RECTOR STREET,INTERSECTION,NEW YORK,,N/A,Closed,10/04/2015 03:01:02 PM,09/18/2015 12:00:00 AM,01 MANHATTAN,MANHATTAN,980656,197137,Unspecified,MANHATTAN,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,Unspecified,N,,,,,,,,,,,,40.70777155363643,-74.01296309970473,\"(40.70777155363643, -74.01296309970473)\", cat");

        if (testRS != null) {

            assertTrue(rs.getAddress() == testRS.getAddress());
            assertTrue(rs.getBorough() == testRS.getBorough());
            assertTrue(rs.getCity() == testRS.getCity());
            assertTrue(rs.getDate() == testRS.getDate());
            assertTrue(rs.getLocation() == testRS.getLocation());
            assertTrue(rs.getKey() == testRS.getKey());
            assertTrue(rs.getLatitude() == testRS.getLatitude());
            assertTrue(rs.getLongitude() == testRS.getLongitude());
        } else {

            assertTrue(false);
        }
    }


    @Test
    public void testParseIncompleteFormat() {

        // A correctly formatted sighting
        RatSighting rs = new RatSighting(31464015, "09/04/2015", "3+ Family Mixed Use Building", 10006, "TRINITY PLACE",
                "MANHATTAN", "MANHATTAN", 40.70777155363643, -74.01296309970473);

        // an incomplete rat string is parsed
        RatSighting testRS = RatSighting.parseEntry("31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM");

            assertTrue(null == testRS.getAddress());
            assertTrue(null == testRS.getBorough());
            assertTrue(null == testRS.getCity());
            assertTrue(rs.getDate() == testRS.getDate());
            assertTrue(null == testRS.getLocation());
            assertTrue(rs.getKey() == testRS.getKey());
            assertTrue(0 == testRS.getLatitude());
            assertTrue(0 == testRS.getLongitude());



        if (testRS != null) {

            // an incomplete rat string is parsed
            testRS = RatSighting.parseEntry("31464015,09/04/2015 12:00:00 AM,09/18/2015 12:00:00 AM,DOHMH,Department of Health and Mental Hygiene,Rodent,Rat Sighting,3+ Family Mixed Use Building,10006,,,,,TRINITY PLACE,RECTOR STREET,INTERSECTION,NEW YORK,,N/A,Closed,10/04/2015 03:01:02 PM");

            assertTrue(rs.getAddress() == testRS.getAddress());
            assertTrue(rs.getBorough() == testRS.getBorough());
            assertTrue(rs.getCity() == testRS.getCity());
            assertTrue(rs.getDate() == testRS.getDate());
            assertTrue(rs.getLocation() == testRS.getLocation());
            assertTrue(rs.getKey() == testRS.getKey());
            assertTrue(0 == testRS.getLatitude());
            assertTrue(0 == testRS.getLongitude());

        }else {

            assertTrue(false);
        }
    }
}
