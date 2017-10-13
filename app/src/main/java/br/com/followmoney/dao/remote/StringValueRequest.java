package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import br.com.followmoney.globals.GlobalParams;

public class StringValueRequest {

    public OnLoadListener onLoadlistener;
    public Context        context;

    public StringValueRequest(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(String restContext) {

        try {

            String URL = GlobalParams.REMOTE_URL + restContext;
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
            }){//here before semicolon ; and use { }.
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map headers = new HashMap();
                    headers.put("Authorization", GlobalParams.getInstance().getAccessToken());
                    return headers;
                }
            };
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
