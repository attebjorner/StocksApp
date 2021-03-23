package com.example.stapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;

public class SearchResultsSingleton
{
    private static SearchResultsSingleton instance;
    private RequestQueue requestQueue;
    private static WeakReference<Context> ctx;

    private SearchResultsSingleton(Context context)
    {
        ctx = new WeakReference<>(context);
        requestQueue = getRequestQueue();
    }

    public static synchronized SearchResultsSingleton getInstance(Context context)
    {
        if (instance == null) instance = new SearchResultsSingleton(context);
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(ctx.get().getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
