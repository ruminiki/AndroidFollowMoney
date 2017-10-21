package br.com.followmoney.domain;

import br.com.followmoney.util.NumberFormatUtil;

/**
 * Created by ruminiki on 21/10/2017.
 */

public class FinalityPieEntry {

    private float value;
    private float percent;
    private String label;

    public FinalityPieEntry(float value, String label) {
        setLabel(label);
        setValue(value);
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getLabel() {
        try{
            return this.label + " (" + NumberFormatUtil.currencyFormat.format(this.getValue()) + ")";
        }catch (Exception e){
            return this.label;
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
