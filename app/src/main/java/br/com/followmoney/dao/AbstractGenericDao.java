package br.com.followmoney.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by ruminiki on 08/09/2017.
 */

public abstract class AbstractGenericDao<T> extends SQLiteOpenHelper {

    public static final String DATABASE_NAME  = "SQLiteExample2.db";
    public static final String COLUMN_ID      = "_id";
    private static final int DATABASE_VERSION = 2;

    public AbstractGenericDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    abstract public void    insert(T object);
    abstract public void    update(T object);
    abstract public void    delete(T object);
    abstract public T       get   (T object);
    abstract public List<T> getAll();

}
