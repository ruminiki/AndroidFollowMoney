package br.com.followmoney.dao.remote.movements;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.followmoney.dao.remote.ApplicationController;
import br.com.followmoney.domain.Movement;

public class PutMovement {

    public OnLoadListener onLoadlistener;
    public Context context;

    public PutMovement(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(Movement movement) {
        try {
            final Gson gson = new Gson();
            String CreditCardJson = gson.toJson(movement);
            System.out.println(CreditCardJson);

            final String URL = "http://192.168.1.13/followMoneyRest/movements/"+movement.getId();
            // Post params to be sent to the server
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, URL, new JSONObject(CreditCardJson),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded(gson.fromJson(response.toString(4), Movement.class));
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

    public interface OnLoadListener {
        void onLoaded(Movement movement);
        void onError(String error);
    }

}
