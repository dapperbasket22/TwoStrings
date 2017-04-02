package pelicula.shiri.twostrings.model;

public class RecommendedObject {
    private int mId;
    private String mPoster, mTitle;

    public RecommendedObject(int id, String poster, String title) {
        mId = id;
        mPoster = poster;
        mTitle = title;
    }

    public int getmId() {
        return mId;
    }

    public String getmPoster() {
        return mPoster;
    }

    public String getmTitle() {
        return mTitle;
    }
}
