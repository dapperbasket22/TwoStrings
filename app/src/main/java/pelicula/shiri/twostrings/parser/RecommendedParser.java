package pelicula.shiri.twostrings.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pelicula.shiri.twostrings.model.RecommendedObject;

public class RecommendedParser {
    private ArrayList<RecommendedObject> mData;

    public RecommendedParser(JSONObject response) {
        mData = new ArrayList<>();
        try{
            JSONArray results = response.getJSONArray("results");
            int length = results.length()>=10?10:results.length();
            for (int i=0; i<length; i++) {
                JSONObject current = results.getJSONObject(i);
                int id = current.getInt("id");
                String poster = current.getString("poster_path");
                String title = current.getString("title");

                mData.add(new RecommendedObject(id, poster, title));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<RecommendedObject> getmData() {
        return mData;
    }
}
