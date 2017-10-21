package br.com.followmoney.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.components.MyGestureListener;
import br.com.followmoney.dao.remote.StringValueRequest;
import br.com.followmoney.globals.GlobalParams;

public class BalanceFragment extends Fragment implements MyGestureListener.OnGestureListener{

    TextView saldoMesTextView, saldoAnteriorTextView, saldoPrevistoTextView, mesReferenciaTextView;
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    GestureDetector gestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.balance_fragment, container, false);
        //saldoMesTextView = (TextView) findViewById(R.id.saldoMesTextView);
        saldoPrevistoTextView = (TextView) view.findViewById(R.id.saldoPrevistoTextView);
        saldoAnteriorTextView = (TextView) view.findViewById(R.id.saldoAnteriorTextView);
        mesReferenciaTextView = (TextView) view.findViewById(R.id.mesReferenciaTextView);
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());

        View balanceView = view.findViewById(R.id.balanceLinearLayout);
        gestureDetector = new GestureDetector(getContext(), new MyGestureListener(this));
        balanceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        loadBalances();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
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
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(context);


        //GET FORESEEN BALANCE
        context = "/movements/previousBalance" +
                "/user/"+ GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getNextMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                saldoPrevistoTextView.setText(numberFormat.format(Double.parseDouble(result)));
                //avi.hide();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(context);

    }

    @Override
    public void leftToRightGesture() {
        GlobalParams.getInstance().setPreviousMonthReference();
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        loadBalances();
    }

    @Override
    public void rightToLeftGesture() {
        GlobalParams.getInstance().setNextMonthReference();
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        loadBalances();
    }
}
