package br.com.followmoney.activities.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.StringValueRequest;
import br.com.followmoney.globals.GlobalParams;

public class MainBalanceActivity extends AppCompatActivity{

    TextView saldoMesTextView, saldoAnteriorTextView, saldoPrevistoTextView;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        //saldoMesTextView = (TextView) findViewById(R.id.saldoMesTextView);
        saldoPrevistoTextView = (TextView) findViewById(R.id.saldoPrevistoTextView);
        saldoAnteriorTextView = (TextView) findViewById(R.id.saldoAnteriorTextView);

        loadBalances();

    }

    protected void loadBalances() {
        //GET PREVIOUS BALANCE
        String context = "/movements/previousBalance" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                saldoAnteriorTextView.setText(numberFormat.format(Double.parseDouble(result)));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);


        //GET FORESEEN BALANCE
        context = "/movements/previousBalance" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getNextMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                saldoPrevistoTextView.setText(numberFormat.format(Double.parseDouble(result)));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);

    }

}
