package br.com.followmoney.globals;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruminiki on 16/09/2017.
 */

public class GlobalParams {

    private static final GlobalParams instance = new GlobalParams();

    private int    userOnLineID;
    private String selectedMonthReference;
    private String selectedMonthReferenceFormated;

    private static final SimpleDateFormat sdfNumber = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdfString = new SimpleDateFormat("MMM/yyyy");

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
            return sdfNumber.format(new Date());
        }
        return selectedMonthReference;
    }

    public void setSelectedMonthReference(String selectedMonthReference) {
        this.selectedMonthReference = selectedMonthReference;
    }

}
