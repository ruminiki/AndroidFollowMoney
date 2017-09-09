package br.com.followmoney.domain;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class Finality {

    private Integer id;
    private String  description;

    public Finality(int id) {
        setId(id);
    }

    public Finality() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + " - " + description;
    }
}
