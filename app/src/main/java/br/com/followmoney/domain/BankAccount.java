package br.com.followmoney.domain;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class BankAccount {

    private Integer id;
    private String  descricao;
    private String  numero;
    private int     digito;
    private String  situacao;
    private Integer usuario;

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

    @Override
    public String toString() {
        return descricao;
    }
}
