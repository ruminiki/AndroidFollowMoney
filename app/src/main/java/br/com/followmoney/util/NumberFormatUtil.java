package br.com.followmoney.util;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by ruminiki on 21/10/2017.
 */

public class NumberFormatUtil {

    public static final DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
    public static final DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(new Locale("pt", "BR"));
    public static final DecimalFormat percentFormat = (DecimalFormat) DecimalFormat.getInstance(new Locale("pt", "BR"));

    {
        currencyFormat.setMaximumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        percentFormat.setMaximumFractionDigits(0);
    }

}
