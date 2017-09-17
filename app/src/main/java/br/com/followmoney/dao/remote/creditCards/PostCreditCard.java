package br.com.followmoney.dao.remote.creditCards;

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
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.util.Params;

public class PostCreditCard {

    public OnLoadListener onLoadlistener;
    public Context context;

    public PostCreditCard(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(CreditCard creditCard) {
        try {
            final Gson gson = new Gson();
            String creditCardJson = gson.toJson(creditCard);
            System.out.println(creditCardJson);

            final String URL = Params.REMOTE_URL + "/creditCards/";
            // Post params to be sent to the server
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(creditCardJson),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded(gson.fromJson(response.toString(4), CreditCard.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    onLoadlistener.onError(error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            ApplicationController.getInstance(context).addToRequestQueue(req);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface OnLoadListener {
        void onLoaded(CreditCard creditCard);
        void onError(String error);
    }


}
