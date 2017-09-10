package br.com.followmoney.dao.remote.finalities;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import br.com.followmoney.dao.remote.HttpHandler;
import br.com.followmoney.domain.Finality;

public class ListFinalities extends AsyncTask<String, Void, List<Finality>> {

    CallBack listener;

    public ListFinalities(CallBack listener) {
        this.listener = listener;
    }

    @Override
    protected List<Finality> doInBackground(String... params) {

        try {

            String url = "http://localhost/followMoneyRest/finalities/user/3";

            String response = HttpHandler.makeServiceCall(url);
            Gson gson = new Gson();

            List<Finality> list = Arrays.asList(gson.fromJson(response,Finality[].class));

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Finality> response) {
        System.out.println("Response: "+response);
        if (response != null) {
            listener.onLoaded(response);
        } else {
            listener.onError();
        }
    }

    public interface CallBack {
        void onLoaded(List<Finality> finalityList);
        void onError();
    }

}
