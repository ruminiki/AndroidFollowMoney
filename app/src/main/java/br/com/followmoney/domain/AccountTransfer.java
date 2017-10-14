package br.com.followmoney.domain;

/**
 * Created by ruminiki on 14/10/2017.
 */

public class AccountTransfer {

    private String descricao;
    private BankAccount contaBancariaOrigem;
    private BankAccount contaBancariaDestino;
    private Finality finalidade;
    private float valor;
    private String data;
    private int usuario;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BankAccount getContaBancariaOrigem() {
        return contaBancariaOrigem;
    }

    public void setContaBancariaOrigem(BankAccount contaBancariaOrigem) {
        this.contaBancariaOrigem = contaBancariaOrigem;
    }

    public BankAccount getContaBancariaDestino() {
        return contaBancariaDestino;
    }

    public void setContaBancariaDestino(BankAccount contaBancariaDestino) {
        this.contaBancariaDestino = contaBancariaDestino;
    }

    public Finality getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(Finality finalidade) {
        this.finalidade = finalidade;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
