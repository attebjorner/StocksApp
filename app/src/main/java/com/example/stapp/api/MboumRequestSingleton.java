package com.example.stapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MboumRequestSingleton
{
    private static MboumRequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MboumRequestSingleton(Context context)
    {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MboumRequestSingleton getInstance(Context context)
    {
        if (instance == null) instance = new MboumRequestSingleton(context);
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
