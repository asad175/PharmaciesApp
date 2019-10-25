package com.asad.pharmaciesapp.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiManager {

    public interface ApiResponseListener {
        void onApiSuccessResponse(String tag, String response);
        void onApiErrorResponse(String tag);
    }

    Context context;
    ApiResponseListener listener;

    public ApiManager(Context context, ApiResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void ExecuteVolleyRequest(final String tag, String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onApiSuccessResponse(tag,response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onApiErrorResponse(tag);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
