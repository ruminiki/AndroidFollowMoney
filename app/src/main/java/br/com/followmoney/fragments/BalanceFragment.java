package br.com.followmoney.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.components.MyGestureListener;
import br.com.followmoney.dao.remote.GetEntityJson;
import br.com.followmoney.domain.Balance;
import br.com.followmoney.globals.GlobalParams;
import br.com.followmoney.util.NumberFormatUtil;

public class BalanceFragment extends Fragment implements MyGestureListener.OnGestureListener{

    TextView saldoMesTextView, saldoAnteriorTextView, saldoPrevistoTextView, mesReferenciaTextView;
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    GestureDetector gestureDetector;

    List<PieEntry> entries = new ArrayList<>();
    PieChart pieChart;
    PieData data = new PieData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.balance_fragment, container, false);
        pieChart              = (PieChart) view.findViewById(R.id.piechart);
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
        //GET BALANCE
        String context = "/movements/balance" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new GetEntityJson<Balance>(new GetEntityJson.OnLoadListener<Balance>() {
            @Override
            public void onLoaded(Balance balance) {
                refreshResume(balance);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(context, new TypeToken<Balance>(){}.getType());

    }

    private void refreshResume(Balance balance){
        saldoAnteriorTextView.setText(numberFormat.format(balance.getSaldoAnterior()));
        saldoPrevistoTextView.setText(numberFormat.format(balance.getSaldoPrevisto()));

        entries.clear();
        entries.add(new PieEntry(balance.getCreditosMes(), "Créditos"));
        entries.add(new PieEntry(balance.getDebitosMes(), "Débitos"));

        PieDataSet dataSet = new PieDataSet(entries, null);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
                /*dataSet.setValueLinePart1Length(0.1f);
                dataSet.setValueLinePart2Length(0.0f);*/
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        data.setDataSet(dataSet);

        pieChart.setUsePercentValues(false);

        data.setValueTextSize(12f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new MyValueFormatter());

        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.setEntryLabelTextSize(10f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(20f);
        pieChart.setHoleRadius(55f);

        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        pieChart.setData(data);
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

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = NumberFormatUtil.currencyFormat; // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value); // e.g. append a dollar-sign
        }
    }
}
