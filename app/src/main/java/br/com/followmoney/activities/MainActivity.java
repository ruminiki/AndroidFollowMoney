package br.com.followmoney.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import br.com.followmoney.R;
import br.com.followmoney.activities.bankAccounts.BankAccountListActivity;
import br.com.followmoney.activities.creditCards.CreditCardListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.activities.movements.MovementCreateOrEditActivity;
import br.com.followmoney.activities.movements.MovementListActivity;
import br.com.followmoney.fragments.BalanceFragment;
import br.com.followmoney.fragments.FinalitySpendingFragment;
import br.com.followmoney.globals.GlobalParams;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fabList = (FloatingActionButton) findViewById(R.id.fabList);
        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabNew = (FloatingActionButton) findViewById(R.id.fabNew);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //mViewPager.setAdapter(mCustomPagerAdapter);
        setupViewPager(viewPager);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initSSLContext();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BalanceFragment(), "BALANCE");
        adapter.addFragment(new FinalitySpendingFragment(), "FINALITIES");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.finality) {
            Intent intent = new Intent(this, FinalityListActivity.class);
            startActivity(intent);
        } else if (id == R.id.credit_card) {
            Intent intent = new Intent(this, CreditCardListActivity.class);
            startActivity(intent);
        } else if (id == R.id.bank_account) {
            Intent intent = new Intent(this, BankAccountListActivity.class);
            startActivity(intent);
        } else if (id == R.id.movements) {
            Intent intent = new Intent(this, MovementListActivity.class);
            startActivity(intent);
        }/*else if (id == R.id.payment_form) {
            Intent intent = new Intent(this, PaymentFormListActivity.class);
            startActivity(intent);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //ssl
    public void initSSLContext() {

        try{
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //InputStream caInput = new BufferedInputStream(new FileInputStream("followmoney.crt"));
            InputStream caInput = getResources().openRawResource(R.raw.followmoney);
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

            SSLSocketFactory sf = context.getSocketFactory();
            GlobalParams.getInstance().setSSLSocketFactory(sf);

        }catch (Exception e){
            e.printStackTrace();
        }

    }



 }
