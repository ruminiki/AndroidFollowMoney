package br.com.followmoney.domain;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class PaymentForm {

    private Integer id;
    private String  descricao;
    private String  sigla;
    private Integer usuario;

    public PaymentForm(int id) {
        setId(id);
    }

    public PaymentForm() {

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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return id + " - " + descricao;
    }
}
