package br.com.followmoney.domain;

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

    public Movement(int id, String descricao, String emissao,
                    String vencimento, String operacao,
                    String finalidade, String contaBancaria,
                    String cartaoCredito) {

        setId(id);
        setDescricao(descricao);
        setEmissao(emissao);
        setVencimento(vencimento);
        setOperacao(operacao);
        if ( !finalidade.isEmpty() && Integer.parseInt(finalidade) > 0)
            setFinalidade(new Finality(Integer.parseInt(finalidade)));
        if ( !contaBancaria.isEmpty() && Integer.parseInt(contaBancaria) > 0)
            setContaBancaria(new BankAccount(Integer.parseInt(contaBancaria)));
        if ( !cartaoCredito.isEmpty() && Integer.parseInt(cartaoCredito) > 0)
            setCartaoCredito(new CreditCard(Integer.parseInt(cartaoCredito)));
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
        return valor;
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

    @Override
    public String toString() {
        return id + " - " + descricao;
    }

   /* @Override
    public Movement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jobject = json.getAsJsonObject();

        return new Movement(
                jobject.get("id").getAsInt(),
                jobject.get("descricao").getAsString(),
                jobject.get("emissao").getAsString(),
                jobject.get("vencimento").getAsString(),
                jobject.get("operacao").getAsString(),
                jobject.get("finalidade").getAsString(),
                jobject.get("contaBancaria").getAsString(),
                jobject.get("cartaoCredito").getAsString());
    }*/
}
