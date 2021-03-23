package com.example.stapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;

public class RequestsSingleton
{
    private static RequestsSingleton instance;
    private RequestQueue requestQueue;
    private static WeakReference<Context> ctx;

    private RequestsSingleton(Context context)
    {
        ctx = new WeakReference<>(context);
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestsSingleton getInstance(Context context)
    {
        if (instance == null) instance = new RequestsSingleton(context);
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
