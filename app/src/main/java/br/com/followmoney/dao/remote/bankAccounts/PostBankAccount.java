package br.com.followmoney.dao.remote.bankAccounts;

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
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.util.Params;

public class PostBankAccount {

    public OnLoadListener onLoadlistener;
    public Context context;

    public PostBankAccount(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(BankAccount bankAccounts) {
        try {
            final Gson gson = new Gson();
            String creditCardJson = gson.toJson(bankAccounts);
            System.out.println(creditCardJson);

            final String URL = Params.REMOTE_URL + "/bankAccounts/";
            // Post params to be sent to the server
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(creditCardJson),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                onLoadlistener.onLoaded(gson.fromJson(response.toString(4), BankAccount.class));
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
        void onLoaded(BankAccount bankAccount);
        void onError(String error);
    }


}
