package br.com.followmoney.dao.remote.creditCards;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import br.com.followmoney.dao.remote.ApplicationController;
import br.com.followmoney.domain.CreditCard;

public class GetCreditCards {

    private OnLoadListener onLoadlistener;
    private Context context;

    public GetCreditCards(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(Integer user) {

        String URL = "http://192.168.1.13/followMoneyRest/creditCards/user/"+user;
        final Gson gson = new Gson();
        // pass second argument as "null" for GET requests
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            onLoadlistener.onLoaded( Arrays.asList(gson.fromJson(response.toString(4),CreditCard[].class)) );
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

    }

    public interface OnLoadListener {
        void onLoaded(List<CreditCard> creditCards);
        void onError(String error);
    }

}
