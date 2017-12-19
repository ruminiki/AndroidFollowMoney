package br.com.followmoney.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class BankAccount {

    public static final String BANK   = "BANK";
    public static final String WALLET = "WALLET";

    public static final String ACTIVE   = "ATIVO";
    public static final String INACTIVE = "INATIVO";

    private Integer id;
    private String  descricao;
    private String  numero;
    private int     digito;
    private String  situacao;
    private String  tipo;
    private Integer usuario;
    private Float   balance;

    public BankAccount(int id) {
        setId(id);
    }

    public BankAccount() {

    }

    public BankAccount(int id, String descricao) {
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

    public String getDescricaoFormatada() {

        if ( this.getTipo().equals(BANK) ){
            return descricao + " (" + this.getNumero()+"-"+this.getDigito() + ")";
        }else{
            return descricao;
        }

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getDigito() {
        return digito;
    }

    public void setDigito(int digito) {
        this.digito = digito;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFormatedBalance() {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(balance);
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
