package br.com.followmoney.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.reflect.TypeToken;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.GetEntitiesJson;
import br.com.followmoney.domain.FinalityPieEntry;
import br.com.followmoney.globals.GlobalParams;
import br.com.followmoney.util.NumberFormatUtil;

public class FinalitySpendingFragment extends Fragment{

    Type type = new TypeToken<List<FinalityPieEntry>>(){}.getType();
    List<PieEntry> yvalues = new ArrayList<PieEntry>();
    PieChart pieChart;
    PieData data = new PieData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.finality_spending_fragment, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);

        final GlobalParams globalParams = GlobalParams.getInstance();
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                loadChartEntries();
            }
        };
        globalParams.changes.addPropertyChangeListener(listener);

        loadChartEntries();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        loadChartEntries();
    }

    protected void loadChartEntries() {
        //GET CHART VALUES
        ///movements/finalitiesChart/user/{user}/period/{period}
        String context = "/movements/finalitiesChart" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new GetEntitiesJson<FinalityPieEntry>(new GetEntitiesJson.OnLoadListener<FinalityPieEntry>() {
            @Override
            public void onLoaded(List<FinalityPieEntry> entries) {
                yvalues.clear();

                for ( FinalityPieEntry entry : entries ){
                    yvalues.add(new PieEntry(entry.getPercent(), entry.getLabel()));
                }

                PieDataSet dataSet = new PieDataSet(yvalues, null);
                //dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setValueLinePart1OffsetPercentage(80.f);
                /*dataSet.setValueLinePart1Length(0.1f);
                dataSet.setValueLinePart2Length(0.0f);*/
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                data.setDataSet(dataSet);

                pieChart.setUsePercentValues(false);

                data.setValueTextSize(9f);
                data.setValueTextColor(Color.DKGRAY);
                data.setValueFormatter(new PercentFormatter(NumberFormatUtil.decimalFormat));

                pieChart.setEntryLabelColor(Color.DKGRAY);
                pieChart.setEntryLabelTextSize(8f);

                pieChart.setDrawHoleEnabled(true);
                pieChart.setTransparentCircleRadius(30f);
                pieChart.setHoleRadius(50f);

                pieChart.setDescription(null);
                pieChart.getLegend().setEnabled(false);

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

                pieChart.setData(data);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(type, context);

    }

}
