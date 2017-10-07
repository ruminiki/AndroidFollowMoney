package br.com.followmoney.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class CreditCard {

    public static final String STATUS_CLOSED = "FECHADA";

    private Integer id;
    private String  descricao;
    private Float   limite;
    private int     dataFatura;
    private int     dataFechamento;
    private Integer usuario;

    public CreditCard(int id) {
        setId(id);
    }

    public CreditCard() {

    }

    public CreditCard(int id, String descricao) {
        setId(id);
        setDescricao(descricao);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getUsuario() { return usuario; }

    public void setUsuario(Integer usuario) { this.usuario = usuario; }

    public Float getLimite() {
        return limite;
    }

    public void setLimite(Float limite) {
        this.limite = limite;
    }

    public int getDataFatura() {
        return dataFatura;
    }

    public void setDataFatura(int dataFatura) {
        this.dataFatura = dataFatura;
    }

    public int getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(int dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getLimiteFormatado(){
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(limite);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
