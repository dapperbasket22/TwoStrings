package pelicula.shiri.twostrings.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.model.MovieObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;

public class MovieParser {
    private ArrayList<MovieObject> mData;

    public MovieParser(JSONObject response) {
        mData = new ArrayList<>();
        try{
            JSONArray results = response.getJSONArray("results");
            for (int i=0; i<results.length(); i++) {
                JSONObject current = results.getJSONObject(i);
                int id = current.getInt("id");
                String poster = current.getString("poster_path");
                String title = current.getString("title");
                float rating = current.getInt("vote_average")/2;
                String userCount = current.getString("vote_count");

                JSONArray genArray = current.getJSONArray("genre_ids");
                String genre = "";
                int glength = genArray.length()>=2?2:genArray.length();
                for (int j=0; j< glength; j++) {
                    genre += CommonMethods.getGenreString(genArray.getInt(j)) + " ";
                }

                mData.add(new MovieObject(id, title, genre, rating, userCount, poster));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<MovieObject> getmData() {
        return mData;
    }
}
