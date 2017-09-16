package br.com.followmoney.activities;

/**
 * Created by ruminiki on 16/09/2017.
 */

public interface SelectableActivity<T> {

    public final static String  KEY_MODE             = "MODE";
    public final static int     OPEN_TO_EDIT_MODE    = 1;
    public final static int     OPEN_TO_SELECT_MODE  = 2;

    public  static final String KEY_ID               = "id";
    public  static final String KEY_DESCRIPTION      = "description";

}
