package pelicula.shiri.twostrings.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pelicula.shiri.twostrings.model.UpMovieObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;

public class UpMovieParser {
    private ArrayList<UpMovieObject> mData;

    public UpMovieParser(JSONObject response) {
        mData = new ArrayList<>();
        try{
            JSONArray results = response.getJSONArray("results");
            int length = results.length()>=10?10:results.length();
            for (int i=0; i<length; i++) {
                JSONObject current = results.getJSONObject(i);
                int id = current.getInt("id");
                String release = CommonMethods.getDate(current.getString("release_date"));
                String backdrop = current.getString("backdrop_path");
                String title = current.getString("title");

                String genre = "";
                JSONArray genreArray = current.getJSONArray("genre_ids");
                int genLength = genreArray.length()>=3?3:genreArray.length();
                for (int j=0; j<genLength; j++)
                    genre += CommonMethods.getGenreString(genreArray.getInt(j)) + " ";

                mData.add(new UpMovieObject(id, title, genre, release, backdrop));
            }
        } catch (JSONException e){
            Log.e("UpMovieParser", e.getMessage());
        }
    }

    public ArrayList<UpMovieObject> getmData() {
        return mData;
    }
}
