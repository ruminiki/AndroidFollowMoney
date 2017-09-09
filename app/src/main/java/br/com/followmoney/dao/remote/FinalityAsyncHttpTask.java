package br.com.followmoney.dao.remote;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.domain.Finality;

public class FinalityAsyncHttpTask extends AsyncTask<String, Void, List<Finality>> {

    Callback listener;

    public FinalityAsyncHttpTask(Callback listener) {
        this.listener = listener;
    }

    @Override
    protected List<Finality> doInBackground(String... params) {

        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("user", "3");

        try {

            String url = "http://www.followmoney.com.br/mobile/finalidade_json.php?usuario=3";

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

    public interface Callback {
        void onLoaded(List<Finality> finalityList);
        void onError();
    }

}
