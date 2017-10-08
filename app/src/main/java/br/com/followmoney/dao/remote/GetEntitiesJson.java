package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import br.com.followmoney.globals.GlobalParams;

public class GetEntitiesJson<T> {

    private OnLoadListener onLoadlistener;
    private Context        context;

    public GetEntitiesJson(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(final Type target, String restContext, String... params) {

        String URL = GlobalParams.REMOTE_URL + restContext;

        //movements/user/:param1/period/:param2
        for ( String param : params ){
            URL = URL.replaceFirst(":param", param);
        }

        final Gson gson = new Gson();
        // pass second argument as "null" for GET requests
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            List<T> result = (List<T>) Arrays.asList(gson.fromJson(response.toString(4), target));
                            onLoadlistener.onLoaded(result != null && result.size() > 0 ? (List<T>) result.get(0) : null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    }

    public interface OnLoadListener<T> {
        void onLoaded(List<T> entities);
        void onError(String error);
    }

}
