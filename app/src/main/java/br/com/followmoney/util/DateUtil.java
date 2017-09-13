package br.com.followmoney.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruminiki on 12/09/2017.
 */

public class DateUtil {

    public static String format(String stringDate, String atualFormat, String newFormat){
        SimpleDateFormat sdfAtual = new SimpleDateFormat(atualFormat);
        SimpleDateFormat sdfNew = new SimpleDateFormat(newFormat);
        try {
            return sdfNew.format(sdfAtual.parse(stringDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return stringDate;
       }
    }

    public static Date parse(String stringDate, String atualFormat) {
        SimpleDateFormat sdfAtual = new SimpleDateFormat(atualFormat);
        try {
            return sdfAtual.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
