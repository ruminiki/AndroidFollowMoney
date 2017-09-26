package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import br.com.followmoney.util.Params;

public class StringValueRequest {

    public OnLoadListener onLoadlistener;
    public Context        context;

    public StringValueRequest(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(String restContext) {

        try {

            String URL = Params.REMOTE_URL + restContext;
            // Instantiate the RequestQueue.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            VolleyLog.v("Response:%n %s", response);
                            onLoadlistener.onLoaded(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    onLoadlistener.onError(error.getMessage());
                }
            });
            // add the request object to the queue to be executed
            ApplicationController.getInstance(context).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnLoadListener {
        void onLoaded(String entity);
        void onError(String error);
    }

}