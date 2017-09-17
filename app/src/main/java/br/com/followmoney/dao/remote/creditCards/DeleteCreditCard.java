package br.com.followmoney.dao.remote.creditCards;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import br.com.followmoney.dao.remote.ApplicationController;
import br.com.followmoney.util.Params;

public class DeleteCreditCard {

    public OnLoadListener onLoadlistener;
    public Context context;

    public DeleteCreditCard(OnLoadListener onLoadlistener, Context context) {
        this.onLoadlistener = onLoadlistener;
        this.context = context;
    }

    public void execute(Integer id) {
        final String URL = Params.REMOTE_URL + "/creditCards/"+id;

        StringRequest req = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        VolleyLog.v("Response:%n %s", response);
                        onLoadlistener.onLoaded(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        onLoadlistener.onError(error.getMessage());
                    }
                }
        );

        // add the request object to the queue to be executed
        ApplicationController.getInstance(context).addToRequestQueue(req);

    }

    public interface OnLoadListener {
        void onLoaded(String response);
        void onError(String error);
    }

}
