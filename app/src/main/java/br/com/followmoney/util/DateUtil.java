package br.com.followmoney.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static Calendar toCalendar(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(Calendar calendar, String format) {
        if ( calendar != null ){
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(calendar.getTime());
        }
        return null;
    }

    public static Calendar getVencimentoMovimentoCartaoCredito(int diaFatura, int diaFechamento) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, diaFatura);
        if ( diaFechamento > diaFatura ){//fatura vence no mes seguinte
            //se a data atual é maior que o fechamento indica que está no intervalo entre o fechamento e o vencimento
            //da fatura. Nesse caso o vencimento será para dois meses a frente
            if ( Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > diaFechamento ){
                c.add(Calendar.MONTH, 2);
            }else{
                c.add(Calendar.MONTH, 1);
            }
        }
        return c;
    }
}
