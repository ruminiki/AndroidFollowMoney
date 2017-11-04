package br.com.followmoney.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.followmoney.util.DateUtil;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class Movement{

    public static final String CREDIT           = "CREDITO";
    public static final String DEBIT            = "DEBITO";
    public static final String CREDIT_INITIALS  = "C";
    public static final String DEBIT_INITIALS   = "D";

    public static final String PAYD    = "PAGO";
    public static final String TO_PAY  = "A PAGAR";

    private Integer           id;
    private String            descricao;
    private String            vencimento;
    private String            emissao;
    private Float             valor;
    private String            status;
    private String            operacao;
    private Finality          finality;
    @SerializedName("bank_account")
    @Expose
    private BankAccount       bankAccount;
    @SerializedName("credit_card")
    @Expose
    private CreditCard        creditCard;
    @SerializedName("payment_form")
    @Expose
    private PaymentForm       paymentForm;
    @SerializedName("invoice")
    @Expose
    private CreditCardInvoice invoice;
    private Integer           usuario;
    private String            hashTransferencia;

    private String            message;

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

    public Finality getFinality() {
        return finality;
    }

    public void setFinality(Finality finality) {
        this.finality = finality;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public PaymentForm getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(PaymentForm paymentForm) {
        this.paymentForm = paymentForm;
    }

    public CreditCardInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(CreditCardInvoice invoice) {
        this.invoice = invoice;
    }

    public String getHashTransferencia() {
        return hashTransferencia;
    }

    public void setHashTransferencia(String hashTransferencia) {
        this.hashTransferencia = hashTransferencia;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getOperacaoResumida(){
        return this.getOperacao().substring(0,1);
    }

    public String getFontePagadora(){
        if ( this.getCreditCard() != null && this.getCreditCard().getId() > 0 ){
            return this.getCreditCard().getDescricao();
        }

        if ( this.getBankAccount() != null && this.getBankAccount().getId() > 0 ){
            return this.getBankAccount().getDescricao();
        }

        return null;
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

    public Boolean canEdit(){

        if ( this.getInvoice() != null && this.getInvoice().getId() > 0 ){
            message = "O movimento é uma invoice de cartão de crédito e não pode ser editado/deletado. Você poderá usar a função de estorno de pagamento.";
            return false;
        }

        if ( this.getHashTransferencia() != null && !this.getHashTransferencia().isEmpty() ){
            message = "O movimento é uma transferência bancária e não pode ser editado. Você poderá usar a função de estorno de transferência.";
            return false;
        }

        return true;
    }

    public Boolean canDelete(){

        if ( this.getInvoice() != null && this.getInvoice().getId() > 0 ){
            message = "O movimento é uma invoice de cartão de crédito e não pode ser editado/deletado. Você poderá usar a função de estorno de pagamento.";
            return false;
        }

        return true;
    }

}
