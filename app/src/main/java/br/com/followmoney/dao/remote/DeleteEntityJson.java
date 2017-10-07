package br.com.followmoney.dao.remote;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

import br.com.followmoney.globals.GlobalParams;

public class DeleteEntityJson {

    public OnResponseListener onResponseListener;
    public Context context;

    public DeleteEntityJson(OnResponseListener onResponseListener, Context context) {
        this.onResponseListener = onResponseListener;
        this.context = context;
    }

    public void execute(String restContext) {
        final String URL = GlobalParams.REMOTE_URL + restContext;

        StringRequest req = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        VolleyLog.v("Response:%n %s", response);
                        onResponseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
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
                                    onResponseListener.onError(parsed);
                            }
                        }
                    }
                }
        );

        // add the request object to the queue to be executed
        ApplicationController.getInstance(context).addToRequestQueue(req);

    }

    public interface OnResponseListener {
        void onResponse(String response);
        void onError(String error);
    }

}
