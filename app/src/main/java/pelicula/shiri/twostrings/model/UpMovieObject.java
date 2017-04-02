package pelicula.shiri.twostrings.model;

public class UpMovieObject {
    private int mId;
    private String mTitle, mGenre, mRelease, mBackdrop;

    public UpMovieObject(int id, String title, String genre, String release, String backdrop) {
        mId = id;
        mTitle = title;
        mGenre = genre;
        mRelease = release;
        mBackdrop = backdrop;
    }

    public int getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmGenre() {
        return mGenre;
    }

    public String getmRelease() {
        return mRelease;
    }

    public String getmBackdrop() {
        return mBackdrop;
    }
}
