package br.com.followmoney.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class CreditCardInvoice {

    private Integer     id;
    private String      descricao;
    private String      emissao;
    private String      vencimento;
    private Float       valor;
    private CreditCard  cartaoCredito;
    private String      mesReferencia;
    private PaymentForm formaPagamento;
    private BankAccount contaBancaria;
    private Float       valorPagamento;
    private String      status;
    private Integer     usuario;

    public CreditCardInvoice(int id) {
        setId(id);
    }

    public CreditCardInvoice() {

    }

    public CreditCardInvoice(int id, String descricao) {
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

    public CreditCard getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CreditCard cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public String getMesReferencia() {
        return mesReferencia.toUpperCase();
    }

    public void setMesReferencia(String mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public PaymentForm getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(PaymentForm formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BankAccount getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(BankAccount contaBancaria) {
        this.contaBancaria = contaBancaria;
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
        return id + " - " + descricao;
    }
}
