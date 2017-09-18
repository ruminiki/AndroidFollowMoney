package br.com.followmoney.domain;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class Finality {

    private Integer id;
    private String  descricao;
    private Integer usuario;

    public Finality(int id) {
        setId(id);
    }

    public Finality() {

    }

    public Finality(int id, String descricao) {
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

    @Override
    public String toString() {
        return id + " - " + descricao;
    }

}
