package pelicula.shiri.twostrings.model;

public class SearchMovieObject extends MovieObject {
    private String mOverview;

    public SearchMovieObject(int id, String title, String genre, float rating, String user,
                             String plot, String poster) {
        super(id, title, genre, rating, user, poster);
        mOverview = plot;
    }

    public String getmOverview() {
        return mOverview;
    }
}
