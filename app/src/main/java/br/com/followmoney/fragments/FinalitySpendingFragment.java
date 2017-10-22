package br.com.followmoney.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.reflect.TypeToken;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.GetEntitiesJson;
import br.com.followmoney.domain.ItemChartEntry;
import br.com.followmoney.globals.GlobalParams;
import br.com.followmoney.util.NumberFormatUtil;

public class FinalitySpendingFragment extends Fragment{

    Type type = new TypeToken<List<ItemChartEntry>>(){}.getType();
    List<BarEntry> entries = new ArrayList<BarEntry>();
    List<String> labels = new ArrayList<String>();
    HorizontalBarChart hBarChart;
    BarData data = new BarData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.finality_spending_fragment, container, false);
        hBarChart = (HorizontalBarChart) view.findViewById(R.id.barchart);

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

        new GetEntitiesJson<ItemChartEntry>(new GetEntitiesJson.OnLoadListener<ItemChartEntry>() {
            @Override
            public void onLoaded(List<ItemChartEntry> entries) {
                FinalitySpendingFragment.this.entries.clear();
                FinalitySpendingFragment.this.labels.clear();
                int index = 0;
                for ( ItemChartEntry entry : entries ){
                    FinalitySpendingFragment.this.entries.add(new BarEntry((float) index++, entry.getPercent()));
                    FinalitySpendingFragment.this.labels.add(entry.getLabel());
                }

                BarDataSet dataSet = new BarDataSet(FinalitySpendingFragment.this.entries, null);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                data.clearValues();
                data.addDataSet(dataSet);

                data.setValueTextSize(10f);
                data.setValueTextColor(Color.DKGRAY);
                data.setValueFormatter(new PercentFormatter(NumberFormatUtil.decimalFormat));

                hBarChart.setDescription(null);
                hBarChart.getLegend().setEnabled(false);

                hBarChart.getAxisRight().setEnabled(false);
                //hBarChart.getXAxis().setEnabled(false);
                hBarChart.getXAxis().setAxisLineColor(Color.TRANSPARENT);
                hBarChart.getAxisLeft().setEnabled(false);
                hBarChart.getXAxis().disableGridDashedLine(); //remove horizontal grid lines
                hBarChart.getXAxis().setDrawGridLines(false);

                //labels
                hBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                hBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                hBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                hBarChart.getXAxis().setTextSize(8f);
                hBarChart.getXAxis().setLabelCount(labels.size());
                hBarChart.getAxisLeft().setSpaceBottom(1);

                hBarChart.notifyDataSetChanged();
                hBarChart.invalidate();

                hBarChart.setData(data);

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(type, context);

    }


}
