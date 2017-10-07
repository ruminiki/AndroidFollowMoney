package br.com.followmoney.globals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruminiki on 16/09/2017.
 */

public class GlobalParams {

    private static final GlobalParams instance = new GlobalParams();

    public final static String REMOTE_URL = "http://192.168.1.17/";

    private int    userOnLineID;
    private String selectedMonthReference;
    private String selectedMonthReferenceFormated;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MMM/yyyy");

    private GlobalParams() { }

    public static GlobalParams getInstance() {
        return instance;
    }

    public int getUserOnLineID() {
        return 3;
    }

    public void setUserOnLineID(int userOnLineID) {
        this.userOnLineID = userOnLineID;
    }

    public String getSelectedMonthReference() {
        if ( selectedMonthReference == null ){
            selectedMonthReference = sdf2.format(new Date());
        }
        return selectedMonthReference;
    }

    public void setSelectedMonthReference(String selectedMonthReference) {
        this.selectedMonthReference = selectedMonthReference;
    }

    public String getSelectedMonthReferenceFormated(){
        try {
            return sdf3.format(sdf1.parse(getSelectedMonthReference() + "01")).toUpperCase();
        } catch (ParseException e) {
            return sdf3.format(new Date()).toUpperCase();
        }
    }

}
