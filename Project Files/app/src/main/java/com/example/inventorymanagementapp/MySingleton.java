package com.example.inventorymanagementapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private Context mCtx;

    private MySingleton(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    //get current request queue of context
    public RequestQueue getRequestQueue(){
        if (requestQueue == null)
            return Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    //get instance of context
    public static synchronized MySingleton getmInstance(Context context){
        if (mInstance == null)
            mInstance = new MySingleton(context);
        return mInstance;
    }

    //add new request to queue
    public<T> void addToRequestQue(Request<T> request){
        getRequestQueue().add(request);
    }
}
