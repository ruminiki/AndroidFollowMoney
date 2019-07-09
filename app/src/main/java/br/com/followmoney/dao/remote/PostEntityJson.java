package br.com.followmoney.dao.remote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import br.com.followmoney.globals.GlobalParams;

public class PostEntityJson<T> extends AsyncTask<String, Void, Boolean> {

    public  OnLoadListener onLoadlistener;
    public  Context        context;
    private Type           target;
    private String         restContext;
    private T              entity;

    private ProgressDialog dialog;

    public PostEntityJson(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(T entity, String restContext, final Type target) {
        this.entity = entity;
        this.target = target;
        this.restContext = restContext;

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading, please wait");
        dialog.setTitle("Saving object...");
        dialog.show();
        dialog.setCancelable(false);

        doInBackground();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            final Gson gson = new Gson();
            String json = gson.toJson(entity);
            System.out.println(json);

            final String URL = GlobalParams.REMOTE_URL + restContext;
            // Post params to be sent to the server
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded(gson.fromJson(response.toString(4), target));
                                dialog.cancel();
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
                    dialog.cancel();
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
            //ApplicationController.getInstance(context).addToRequestQueue(req);

            RequestQueue rq = Volley.newRequestQueue(context, new HurlStack(null, GlobalParams.getInstance().sslSocketFactory));
            rq.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnLoadListener<T> {
        void onLoaded(T entity);
        void onError(String error);
    }


}
