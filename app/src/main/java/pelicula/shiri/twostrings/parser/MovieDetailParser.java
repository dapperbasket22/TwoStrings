package pelicula.shiri.twostrings.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pelicula.shiri.twostrings.model.MovieDetailObject;
import pelicula.shiri.twostrings.utilities.CommonMethods;

public class MovieDetailParser {
    private MovieDetailObject mMovieData;
    private RecommendedParser mMovieRecommended;

    public MovieDetailParser(JSONObject response) {
        try {
            String genre = "";
            JSONArray genreArray = response.getJSONArray("genres");
            int genLength = genreArray.length()>=2?2:genreArray.length();
            for (int i=0; i<genLength; i++) {
                JSONObject currentGenre = genreArray.getJSONObject(i);
                genre += currentGenre.getString("name") + " ";
            }

            int id = response.getInt("id");
            String overview = response.getString("overview");
            String poster = response.getString("backdrop_path");
            String release = CommonMethods.getDate(response.getString("release_date"));
            String runtime = response.getString("runtime") + " min";
            String title = response.getString("title");
            float rating = (float) response.getDouble("vote_average");
            String users = response.getString("vote_count");
            String imdb_id = response.getString("imdb_id");

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

            mMovieData = new MovieDetailObject(id, imdb_id, title, genre, rating, users, overview, release,
                    runtime, poster, videoKey);
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
}
