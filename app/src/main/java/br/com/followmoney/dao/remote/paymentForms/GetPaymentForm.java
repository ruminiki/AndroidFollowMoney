package br.com.followmoney.dao.remote.paymentForms;

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
import br.com.followmoney.domain.PaymentForm;

public class GetPaymentForm {

    public OnLoadListener onLoadlistener;
    public Context context;

    public GetPaymentForm(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(Integer id) {

        try {

            String URL = "http://192.168.1.13/followMoneyRest/paymentForms/"+id;
            final Gson gson = new Gson();
            // pass second argument as "null" for GET requests
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                PaymentForm f = gson.fromJson(response.toString(4),PaymentForm.class);
                                onLoadlistener.onLoaded(f);
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
        void onLoaded(PaymentForm paymentForm);
        void onError(String error);
    }

}
