package pelicula.shiri.twostrings.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pelicula.shiri.twostrings.model.TdObject;

public class CommonMethods {

    public static String getDate(String date){
        if (TextUtils.isEmpty(date)) return "";
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        Date dateString;
        try {
            dateString = originalFormat.parse(date);
        } catch (ParseException e) {
            Log.e("DateError", e.getMessage());
            return "";
        }
        return targetFormat.format(dateString);
    }

    public static String getYear(String date){
        if (TextUtils.isEmpty(date)) return "";
        DateFormat originalFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        Date dateString;
        try {
            dateString = originalFormat.parse(date);
        } catch (ParseException e) {
            Log.e("DateError", e.getMessage());
            return "";
        }
        return targetFormat.format(dateString);
    }

    public static ArrayList<TdObject> getGenreData() {
        ArrayList<TdObject> data = new ArrayList<>();

        data.add(new TdObject(28, "Action"));
        data.add(new TdObject(12, "Adventure"));
        data.add(new TdObject(16, "Animation"));
        data.add(new TdObject(35, "Comedy"));
        data.add(new TdObject(80, "Crime"));
        data.add(new TdObject(99, "Documentary"));
        data.add(new TdObject(18, "Drama"));
        data.add(new TdObject(10751, "Family"));
        data.add(new TdObject(14, "Fantasy"));
        data.add(new TdObject(36, "History"));
        data.add(new TdObject(27, "Horror"));
        data.add(new TdObject(10402, "Music"));
        data.add(new TdObject(9648, "Mystery"));
        data.add(new TdObject(10749, "Romance"));
        data.add(new TdObject(878, "Fiction"));
        data.add(new TdObject(53, "Thriller"));
        data.add(new TdObject(10752, "War"));
        data.add(new TdObject(37, "Western"));

        return data;
    }

    public static String getGenreString(int gId){
        switch (gId){
            case 28: return "Action";
            case 12: return "Adventure";
            case 16: return "Animation";
            case 35: return "Comedy";
            case 80: return "Crime";
            case 99: return "Documentary";
            case 18: return "Drama";
            case 10751: return "Family";
            case 14: return "Fantasy";
            case 36: return "History";
            case 27: return "Horror";
            case 10402: return "Music";
            case 9648: return "Mystery";
            case 10749: return "Romance";
            case 878: return "Fiction";
            case 10770: return "TV";
            case 53: return "Thriller";
            case 10752: return "War";
            case 37: return "Western";
            default: return "";
        }
    }
}
