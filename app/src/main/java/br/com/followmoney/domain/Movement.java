package br.com.followmoney.domain;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.followmoney.util.DateUtil;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class Movement{

    public static final String CREDIT = "CREDITO";
    public static final String DEBIT  = "DEBITO";
    private Integer     id;
    private String      descricao;
    private String      vencimento;
    private String      emissao;
    private Float       valor;
    private String      status;
    private String      operacao;
    private Finality    finalidade;
    private BankAccount contaBancaria;
    private CreditCard  cartaoCredito;
    private PaymentForm formaPagamento;
    private Integer     usuario;

    public Movement(int id) {
        setId(id);
    }

    public Movement() {
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

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public Float getValor() {
        return valor != null ? valor : 0f;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public Finality getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(Finality finalidade) {
        this.finalidade = finalidade;
    }

    public BankAccount getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(BankAccount contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public CreditCard getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CreditCard cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public PaymentForm getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(PaymentForm formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getEmissaoFormatado(){
        return DateUtil.format(emissao, "yyyyMMdd", "dd/MM/yyyy");
    }

    public String getVencimentoFormatado(){
        return DateUtil.format(vencimento, "yyyyMMdd", "dd/MM/yyyy");
    }

    public String getValorFormatado(){
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }

    @Override
    public String toString() {
        return id + " - " + descricao;
    }

    public Calendar getEmissionDate(){
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, Integer.parseInt(getEmissao().substring(1, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(getEmissao().substring(5, 6)));
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getEmissao().substring(7, 8)));
        return c;

    }

    public Calendar getMaturityDate(){
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, Integer.parseInt(getVencimento().substring(1, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(getVencimento().substring(5, 6)));
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getVencimento().substring(7, 8)));
        return c;

    }

}
