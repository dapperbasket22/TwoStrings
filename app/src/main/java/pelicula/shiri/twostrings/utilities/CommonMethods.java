package pelicula.shiri.twostrings.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonMethods {

    public static String getDate(String date){
        if (date.equals("") || date.equals("null") || date.isEmpty()) return "";
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        Date dateString = null;
        try {
            dateString = originalFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetFormat.format(dateString);
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
