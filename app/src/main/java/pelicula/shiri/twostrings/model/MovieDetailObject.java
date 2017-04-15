package pelicula.shiri.twostrings.model;

public class MovieDetailObject extends MovieObject {
    private String mRelease, mRuntime, mTrailer, mImdbId;

    public MovieDetailObject(int id, String imdbId, String title, String genre, float rating, String user,
                             String plot, String release, String runtime, String poster,
                             String trailer) {
        super(id, title, genre, rating, user, plot, poster); //Poster is Backdrop
        mImdbId = imdbId;
        mRelease = release;
        mRuntime = runtime;
        mTrailer = trailer;
    }

    public String getmImdbId() {
        return mImdbId;
    }

    public String getmRelease() {
        return mRelease;
    }

    public String getmRuntime() {
        return mRuntime;
    }

    public String getmTrailer() {
        return mTrailer;
    }
}
