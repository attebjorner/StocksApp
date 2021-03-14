package com.example.stapp.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stapp.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.stapp.api.SearchResultsDataApi.getSearchResultsData;

public class SearchResultsApi
{
    public static void getSearchResults(Context context, View rootView, String searchQuery)
    {
        final StringBuffer[] queryResults = {new StringBuffer()};
//        String LOOKUP_REQUEST = "https://finnhub.io/api/v1/search?q=" + searchQuery
//                + "&token=c152grv48v6r76ch3rq0";
        String LOOKUP_REQUEST = "https://finnhub.io/api/v1/search?q=" + "apple"
                + "&token=c152grv48v6r76ch3rq0";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, LOOKUP_REQUEST, null, response ->
        {
            try
            {
                JSONArray quotesArray = (JSONArray) response.get("result");
                for (int i = 0; i < quotesArray.length(); i++)
                {
                    JSONObject temp = quotesArray.getJSONObject(i);
                    queryResults[0].append(temp.get("symbol").toString());
                    if (i != quotesArray.length() - 1) queryResults[0].append(",");
                }
            } catch (JSONException e) { e.printStackTrace(); }

            //TODO: get comma-separated string query results and then search for them
            getSearchResultsData(context, rootView, queryResults[0].toString());

        }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        );

        jsonObjectRequest.setTag("searchRequest");
        SearchResultSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
