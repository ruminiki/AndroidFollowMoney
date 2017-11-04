package br.com.followmoney.domain;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class CreditCardInvoice {

    public static final String OPEN  = "ABERTA";
    public static final String CLOSE = "FECHADA";

    private Integer     id;
    private String      emissao;
    private String      vencimento;
    private Float       valor;
    @SerializedName("credit_card")
    private CreditCard  creditCard;
    private String      mesReferencia;
    @SerializedName("payment_form")
    private PaymentForm paymentForm;
    @SerializedName("bank_account")
    private BankAccount bankAccount;
    private Float       valorPagamento;
    private String      status;
    private Integer     usuario;

    public CreditCardInvoice(int id) {
        setId(id);
    }

    public CreditCardInvoice() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getMesReferencia() {
        return mesReferencia.toUpperCase();
    }

    public void setMesReferencia(String mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public PaymentForm getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(PaymentForm paymentForm) {
        this.paymentForm = paymentForm;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Float getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(Float valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getValorFormatado() {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }

    @Override
    public String toString() {
        return id + " - " + mesReferencia;
    }
}
