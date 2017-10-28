package br.com.followmoney.globals;

import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ruminiki on 16/09/2017.
 */

public class GlobalParams {

    private static final GlobalParams instance = new GlobalParams();

    public final static String REMOTE_URL = "https://followmoney.com.br";
    //public final static String REMOTE_URL = "http://192.168.1.12";

    private int    userOnLineID = 3;
    private String accessToken = "7be2531ea6205505f6bd851d9c1f8199e83eafda";
    private String selectedMonthReference;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MMM/yyyy");

    public final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private GlobalParams() { }

    public static GlobalParams getInstance() {
        return instance;
    }

    public int getUserOnLineID() {
        return userOnLineID;
    }

    public void setUserOnLineID(int userOnLineID) {
        this.userOnLineID = userOnLineID;
    }

    public String getSelectedMonthReference() {
        if ( selectedMonthReference == null ){
            setSelectedMonthReference(sdf2.format(new Date()));
        }
        return selectedMonthReference;
    }

    public String getNextMonthReference() {
        try {

            if ( selectedMonthReference == null ){
                setSelectedMonthReference(sdf2.format(new Date()));
            }

            Date atualMonth = sdf1.parse(selectedMonthReference+"01");
            Calendar nextMonth = Calendar.getInstance();

            nextMonth.setTime(atualMonth);
            nextMonth.add(Calendar.MONTH, 1);

            return sdf2.format(nextMonth.getTime());

        } catch (ParseException e) {
            return selectedMonthReference;
        }
    }

    public void setNextMonthReference() {
        try {

            if ( selectedMonthReference == null ){
                setSelectedMonthReference(sdf2.format(new Date()));
            }

            Date atualMonth = sdf1.parse(selectedMonthReference+"01");
            Calendar nextMonth = Calendar.getInstance();

            nextMonth.setTime(atualMonth);
            nextMonth.add(Calendar.MONTH, 1);

            setSelectedMonthReference(sdf2.format(nextMonth.getTime()));

        } catch (ParseException e) {}
    }

    public void setPreviousMonthReference() {
        try {

            if ( selectedMonthReference == null ){
                setSelectedMonthReference(sdf2.format(new Date()));
            }

            Date atualMonth = sdf1.parse(selectedMonthReference+"01");
            Calendar previousMonth = Calendar.getInstance();

            previousMonth.setTime(atualMonth);
            previousMonth.add(Calendar.MONTH, -1);

            setSelectedMonthReference(sdf2.format(previousMonth.getTime()));

        } catch (ParseException e) {}
    }

    public void setSelectedMonthReference(String newValue) {
        String oldValue = this.selectedMonthReference;
        this.selectedMonthReference = newValue;
        this.changes.firePropertyChange("selectedMonthReference",oldValue, newValue);
    }

    public String getSelectedMonthReferenceFormated(){
        try {
            return sdf3.format(sdf1.parse(getSelectedMonthReference() + "01")).toUpperCase();
        } catch (ParseException e) {
            return sdf3.format(new Date()).toUpperCase();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }
}
