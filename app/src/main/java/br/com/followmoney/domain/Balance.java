package br.com.followmoney.domain;

/**
 * Created by ruminiki on 23/10/2017.
 */

public class Balance {

    private float saldoAnterior;
    private float saldoPrevisto;
    private float debitosMes;
    private float creditosMes;

    public float getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(float saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public float getSaldoPrevisto() {
        return saldoPrevisto;
    }

    public void setSaldoPrevisto(float saldoPrevisto) {
        this.saldoPrevisto = saldoPrevisto;
    }

    public float getDebitosMes() {
        return debitosMes;
    }

    public void setDebitosMes(float debitosMes) {
        this.debitosMes = debitosMes;
    }

    public float getCreditosMes() {
        return creditosMes;
    }

    public void setCreditosMes(float creditosMes) {
        this.creditosMes = creditosMes;
    }
}
