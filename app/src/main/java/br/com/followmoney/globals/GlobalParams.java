package br.com.followmoney.globals;

import android.content.res.AssetManager;
import android.util.Log;

import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import br.com.followmoney.R;

/**
 * Created by ruminiki on 16/09/2017.
 */

public class GlobalParams {

    private static final GlobalParams instance = new GlobalParams();

    public final static String REMOTE_URL = "https://followmoney.com.br";
    //public final static String REMOTE_URL = "http://192.168.1.18";

    private int    userOnLineID = 3;
    private String accessToken = "1ED8B6E8BC436EC8E744FBF638793";
    private String selectedMonthReference;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MMM/yyyy");

    public final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public SSLSocketFactory sslSocketFactory;

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

    public void initSSLContext(InputStream caInput) {
        try{
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo("followmoney.com.br")==0; //The Hostname of your server
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            // Create an SSLContext that uses our TrustManager
            javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            this.sslSocketFactory = context.getSocketFactory();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory){
        this.sslSocketFactory = sslSocketFactory;
    }

    public SSLSocketFactory getSSLSocketFactory(){
        return this.sslSocketFactory;
    }
}
