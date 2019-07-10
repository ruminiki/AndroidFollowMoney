package br.com.followmoney.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.reflect.TypeToken;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.KeyParams;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.activities.movements.MovementListActivity;
import br.com.followmoney.dao.remote.GetEntitiesJson;
import br.com.followmoney.domain.ItemChartEntry;
import br.com.followmoney.globals.GlobalParams;
import br.com.followmoney.util.NumberFormatUtil;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;
import static br.com.followmoney.activities.KeyParams.KEY_FILL_FINALITY_PARAM;

public class FinalitySpendingFragment extends Fragment implements OnChartValueSelectedListener {

    Type type = new TypeToken<List<ItemChartEntry>>(){}.getType();
    List<BarEntry> entries = new ArrayList<BarEntry>();
    List<String> labels = new ArrayList<String>();
    HorizontalBarChart hBarChart;
    BarData data = new BarData();
    TextView mesTextView;

    private boolean active = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.finality_spending_fragment, container, false);
        hBarChart = (HorizontalBarChart) view.findViewById(R.id.barchart);
        hBarChart.setOnChartValueSelectedListener(this);
        mesTextView = (TextView) view.findViewById(R.id.mesTextView);
        mesTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());

        final GlobalParams globalParams = GlobalParams.getInstance();
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if ( active && !GlobalParams.getInstance().getSelectedMonthReferenceFormated().equals(mesTextView.getText()) ){
                    loadChartEntries();
                    mesTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
                }
            }
        };
        globalParams.changes.addPropertyChangeListener(listener);

        loadChartEntries();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;

        if ( !GlobalParams.getInstance().getSelectedMonthReferenceFormated().equals(mesTextView.getText()) ){
            loadChartEntries();
            mesTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
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
                    FinalitySpendingFragment.this.entries.add(new BarEntry((float) index++, entry.getPercent(), entry.getLabel()));
                    FinalitySpendingFragment.this.labels.add(entry.getLabel());
                }

                BarDataSet dataSet = new BarDataSet(FinalitySpendingFragment.this.entries, null);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                data.clearValues();
                data.addDataSet(dataSet);
                data.setValueFormatter(new ValueFormat(new DecimalFormat("###,###,###")));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.DKGRAY);
                //data.setValueFormatter(new PercentFormatter(NumberFormatUtil.percentFormat));

                hBarChart.setData(data);

                hBarChart.setDescription(null);
                hBarChart.getLegend().setEnabled(false);

                hBarChart.getAxisRight().setEnabled(false);
                //hBarChart.getXAxis().setEnabled(false);
                hBarChart.getXAxis().setAxisLineColor(Color.TRANSPARENT);
                hBarChart.getAxisLeft().setEnabled(false);
                hBarChart.getXAxis().disableGridDashedLine(); //remove horizontal grid lines
                hBarChart.getXAxis().setDrawGridLines(false);

                //scroll
                //hBarChart.setVisibleXRangeMaximum(15);
                //hBarChart.setVisibleXRange(15,15);

                //labels
                hBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                hBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                hBarChart.getXAxis().setTextSize(8f);
                hBarChart.getAxisLeft().setSpaceBottom(1);
                hBarChart.getXAxis().setCenterAxisLabels(false);
                hBarChart.getXAxis().setDrawLabels(true);
                hBarChart.getXAxis().setLabelCount(labels.size());


                //hBarChart.setVisibleXRange(0, 15);

                //belCount((int)hBarChart.getVisibleXRange());
               // hBarChart.moveViewToX(0);

                //no zoom
                hBarChart.setScaleEnabled(false);
                //hBarChart.setMinimumHeight(350);
                hBarChart.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

                hBarChart.notifyDataSetChanged();
                hBarChart.invalidate();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(type, context);

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String finalitie = ((String)e.getData()).substring(0,((String)e.getData()).lastIndexOf('('));
        Intent intent = new Intent(this.getActivity(), MovementListActivity.class);
        intent.putExtra(KEY_FILL_FINALITY_PARAM, finalitie);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {

    }

    public class ValueFormat extends PercentFormatter {

        protected DecimalFormat mFormat;

        public ValueFormat(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " %";
        }
    }
}
