package br.com.followmoney.dao.remote.finalities;

import com.google.gson.Gson;

import br.com.followmoney.dao.remote.HttpHandler;
import br.com.followmoney.domain.Finality;

public class UpdateFinality {

    public UpdateFinality() {  }

    public static Finality execute(Integer id) {

        try {
            //@TODO: method put
            String url = "http://localhost/followMoneyRest/finalities/"+id;

            String response = HttpHandler.makeServiceCall(url);
            Gson gson = new Gson();

            Finality finality = gson.fromJson(response,Finality.class);

            return finality;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
