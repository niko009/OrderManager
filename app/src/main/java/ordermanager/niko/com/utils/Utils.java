package ordermanager.niko.com.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void Log(String tag, String message, char type) {

    }

    public static void Log(String tag, String message) {
        Log.v(tag, message);
    }

    public static void LogE(String tag, String message) {

    }

    public static Date getDate(String formatString, String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = new Date();
        try {
            date = format.parse(dateString);
            //
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
