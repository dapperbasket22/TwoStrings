package pelicula.shiri.twostrings.model;

public class CastObject {
    private int mId;
    private String mName, mCharacter, mProfile;

    public CastObject(int id, String name, String character, String profile) {
        mId = id;
        mName = name;
        mCharacter = character;
        mProfile = profile;
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmCharacter() {
        return mCharacter;
    }

    public String getmProfile() {
        return mProfile;
    }
}
