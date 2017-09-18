package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

import br.com.followmoney.util.Params;

public class PutEntityJson<T> {

    public OnLoadListener onLoadlistener;
    public Context        context;

    public PutEntityJson(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(T entity, Integer id, String restContext) {

        try {
            final Gson gson = new Gson();
            String json = gson.toJson(entity);
            System.out.println(json);

            final String URL = Params.REMOTE_URL + "/"+ restContext +"/"+id;
            // Post params to be sent to the server
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, URL, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded((T) gson.fromJson(response.toString(4), ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
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
