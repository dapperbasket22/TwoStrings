package pelicula.shiri.twostrings.model;

public class MovieDetailObject extends SearchMovieObject {
    private String mRelease, mRuntime, mBackdrop, mTrailer;

    public MovieDetailObject(int id, String title, String genre, float rating, String user,
                             String plot, String release, String runtime, String poster,
                             String backdrop, String trailer) {
        super(id, title, genre, rating, user, plot, poster);
        mRelease = release;
        mRuntime = runtime;
        mBackdrop = backdrop;
        mTrailer = trailer;
    }

    public String getmRelease() {
        return mRelease;
    }

    public String getmRuntime() {
        return mRuntime;
    }

    public String getmBackdrop() {
        return mBackdrop;
    }

    public String getmTrailer() {
        return mTrailer;
    }
}
