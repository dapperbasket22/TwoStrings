package pelicula.shiri.twostrings.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.model.CastObject;

public class CreditsParser {
    private ArrayList<CastObject> mCastData;

    public CreditsParser(JSONObject response) {
        mCastData = new ArrayList<>();
        try{
            JSONArray results = response.getJSONArray("cast");
            for (int i=0; i<results.length(); i++) {
                JSONObject current = results.getJSONObject(i);
                int id = current.getInt("id");
                String character = current.getString("character");
                String name = current.getString("name");
                String poster = current.getString("profile_path");

                mCastData.add(new CastObject(id, name, character, poster));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<CastObject> getmCastData() {
        return mCastData;
    }
}
