package pelicula.shiri.twostrings.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pelicula.shiri.twostrings.model.MovieDetailObject;
import pelicula.shiri.twostrings.model.TdObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;

public class MovieDetailParser {
    private MovieDetailObject mMovieData;
    private RecommendedParser mMovieRecommended;
    private ArrayList<TdObject> mMovieKeyword;

    public MovieDetailParser(JSONObject response) {
        mMovieKeyword = new ArrayList<>();
        try {
            String backdrop = response.getString("backdrop_path");

            String genre = "";
            JSONArray genreArray = response.getJSONArray("genres");
            int genLength = genreArray.length()>=2?2:genreArray.length();
            for (int i=0; i<genLength; i++) {
                JSONObject currentGenre = genreArray.getJSONObject(i);
                genre += currentGenre.getString("name") + " ";
            }

            int id = response.getInt("id");
            String overview = response.getString("overview");
            String poster = response.getString("poster_path");
            String release = CommonMethods.getDate(response.getString("release_date"));
            String runtime = response.getString("runtime") + " min";
            String title = response.getString("title");
            float rating = response.getInt("vote_average")/2;
            String users = response.getString("vote_count");

            String videoKey = "";
            JSONObject videoObject = response.getJSONObject("videos");
            JSONArray videoArray = videoObject.getJSONArray("results");
            for (int i=0; i<videoArray.length(); i++){
                JSONObject currentVideo = videoArray.getJSONObject(i);
                if (currentVideo.getString("site").equals("YouTube")) {
                    videoKey = currentVideo.getString("key");
                    break;
                }
            }
            if (videoKey.equals("")) videoKey = null;

            mMovieRecommended = new RecommendedParser(response.getJSONObject("recommendations"));

            JSONObject keywordObject = response.getJSONObject("keywords");
            JSONArray keywordArray = keywordObject.getJSONArray("keywords");
            int keywordLength = keywordArray.length()>=6?6:keywordArray.length();
            for (int i=0; i<keywordLength; i++){
                JSONObject current = keywordArray.getJSONObject(i);
                mMovieKeyword.add(new TdObject(current.getInt("id"), current.getString("name")));
            }

            mMovieData = new MovieDetailObject(id, title, genre, rating, users, overview, release,
                    runtime, poster, backdrop, videoKey);
        } catch (JSONException error){
            error.printStackTrace();
        }
    }

    public MovieDetailObject getmMovieData() {
        return mMovieData;
    }

    public RecommendedParser getmMovieRecommended() {
        return mMovieRecommended;
    }

    public ArrayList<TdObject> getmMovieKeyword() {
        return mMovieKeyword;
    }
}
