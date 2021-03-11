package com.example.stapp.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stapp.ListItem;
import com.example.stapp.MainListAdapter;
import com.example.stapp.R;
import com.example.stapp.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class MboumApi
{
    public static void getMboumStocks(Context context, View rootView)
    {
        ArrayList<ListItem> stocksResponseItems = new ArrayList<>();
//        JSONParser jsonParser = new JSONParser();

//        URL url = new URL(REQUEST_URL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder responseData = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) responseData.append(inputLine.trim());
//        in.close();
//        connection.disconnect();

//        RequestQueue queue = Volley.newRequestQueue(context);
        String REQUEST_URL = "https://mboum.com/api/v1/co/collections/?list=most_actives&start=1&" +
                "apikey=xThUx6KqmURgMfT1xXXjcT8GIpB1zeJ6ghcWqT3eHeOTlJTtw9DnDq9qWP2g";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, REQUEST_URL, null, response ->
                {
                    JSONArray quotesArray = null;
                    try
                    {
                        quotesArray = (JSONArray) response.get("quotes");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < quotesArray.length(); i++)
                    {
                        try
                        {
                            JSONObject temp = quotesArray.getJSONObject(i);
                            stocksResponseItems.add(new ListItem(
                                    temp.get("symbol").toString(), temp.get("shortName").toString(),
                                    temp.get("regularMarketPrice").toString(), temp.get("regularMarketChange").toString(),
                                    temp.get("regularMarketChangePercent").toString()
                            ));
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    TinyDB tinyDB = new TinyDB(context);
                    ArrayList<String> favorites;
                    favorites = tinyDB.getListString("favorites");

                    for (int i = 0; i < stocksResponseItems.size(); i++)
                    {
                        if (favorites.contains(stocksResponseItems.get(i).getSymbol()))
                        {
                            stocksResponseItems.get(i).setFavorite(true);
                        }
                    }

                    tinyDB.putObject("mainStocks", new StocksContainer(LocalDate.now(), stocksResponseItems));

                    RecyclerView rvStocks = (RecyclerView) rootView.findViewById(R.id.rvStocks);
                    LinearLayoutManager llManager = new LinearLayoutManager(context);
                    rvStocks.setLayoutManager(llManager);
                    MainListAdapter adapter = new MainListAdapter(stocksResponseItems, context);
                    rvStocks.setAdapter(adapter);
                }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        );

//        queue.add(jsonObjectRequest);
        MboumRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, REQUEST_URL,
//                response ->
//                {
//                    JSONObject json = new JSONObject();
//                    try
//                    {
//                        json = (JSONObject) jsonParser.parse(response);
//                    } catch (ParseException e)
//                    {
//                        e.printStackTrace();
//                    }
//                    JSONArray quotesArray = (JSONArray) json.get("quotes");
//                    for (Object o : quotesArray)
//                    {
//                        JSONObject temp = (JSONObject) o;
//                        stocksResponseItems.add(new ListItem(
//                                temp.get("symbol").toString(), temp.get("shortName").toString(),
//                                temp.get("regularMarketPrice").toString(), temp.get("regularMarketChange").toString(),
//                                temp.get("regularMarketChangePercent").toString()
//                        ));
//                    }
//                    TinyDB tinyDB = new TinyDB(context);
//                    ArrayList<String> favorites;
//                    favorites = tinyDB.getListString("favorites");
//
//                    for (int i = 0; i < stocksResponseItems.size(); i++)
//                    {
//                        if (favorites.contains(stocksResponseItems.get(i).getSymbol()))
//                        {
//                            stocksResponseItems.get(i).setFavorite(true);
//                        }
//                    }
//
//                    tinyDB.putObject("mainStocks", new StocksContainer(LocalDate.now(), stocksResponseItems));
//                }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show());
//
//        queue.add(stringRequest);
    }
}
