package br.com.followmoney.dao.remote;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

    private List<T> list = new ArrayList<T>();

    public List<T> getList() {
        return list;
    }
}