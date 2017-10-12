package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import br.com.followmoney.globals.GlobalParams;

public class GetEntityJson<T> {

    public OnLoadListener onLoadlistener;
    public Context        context;

    public GetEntityJson(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(String restContext, final Type target) {

        try {

            String URL = GlobalParams.REMOTE_URL + restContext;
            //final Gson gson = new Gson();
            final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            // pass second argument as "null" for GET requests
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded(gson.fromJson(response.toString(4), target));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    String parsed;
                    NetworkResponse networkResponse = error.networkResponse;
                    if(networkResponse != null && networkResponse.data != null) {
                        try {
                            parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
                        } catch (UnsupportedEncodingException var4) {
                            parsed = new String(networkResponse.data);
                        }
                        NetworkResponse response = new NetworkResponse(networkResponse.data);
                        Response<String> parsedResponse;
                        switch(response.statusCode){
                            default:
                                onLoadlistener.onError(parsed);
                        }
                    }
                    onLoadlistener.onError("Error on server get object.");
                }
            });

            // add the request object to the queue to be executed
            ApplicationController.getInstance(context).addToRequestQueue(req);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnLoadListener<T> {
        void onLoaded(T entity);
        void onError(String error);
    }

}
