package pelicula.shiri.twostrings.model;

import java.io.Serializable;

public class TdObject implements Serializable {
    private int mId;
    private String mName;

    public TdObject(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }
}
