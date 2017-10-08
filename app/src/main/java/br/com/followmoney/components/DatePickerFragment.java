package br.com.followmoney.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ruminiki on 08/10/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public OnDateSetListener onDateSetListener;
    private int Ano, Mes, Dia, Hora, Minuto;

    Calendar calendar = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if ( calendar == null ){
            calendar = Calendar.getInstance();
            Ano = calendar.get(Calendar.YEAR);
            Mes = calendar.get(Calendar.MONTH);
            Dia = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, Ano, Mes, Dia);
    }

    public void setDate(Calendar calendar){
        Ano = calendar.get(Calendar.YEAR);
        Mes = calendar.get(Calendar.MONTH);
        Dia = calendar.get(Calendar.DAY_OF_MONTH);
        this.calendar = calendar;
    }

    public Calendar getDate(){
        return calendar;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag){
        return super.show(transaction, tag);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Ano = year;
        Mes = month;
        Dia = dayOfMonth;
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        this.onDateSetListener.onDateSet(calendar);
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public interface OnDateSetListener {
        void onDateSet(Calendar calendar);
    }

}
