package pelicula.shiri.twostrings.utilities;

import pelicula.shiri.twostrings.BuildConfig;

public class TMAUrl {
    //Movies
    public static final String UPCOMING_MOVIE_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key="
            + BuildConfig.TMDB_API_KEY + "&region=in";
    public static final String POPULAR_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.TMDB_API_KEY;
    public static final String EXPLORE_MOVIE_URL =
            "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.TMDB_API_KEY+
                    "&sort_by=popularity.desc" + "&with_genres=";
    public static final String MOVIE_DETAIL_URL = "https://api.themoviedb.org/3/movie";

    //Image
    public static final String IMAGE_MED_URL = "https://image.tmdb.org/t/p/w300/";

    //Video
    public static final String YOU_TUBE_URL = "https://www.youtube.com/watch?v=";

    //Search
    public static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key="
            + BuildConfig.TMDB_API_KEY;
}
