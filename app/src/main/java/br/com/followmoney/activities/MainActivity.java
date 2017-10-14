package br.com.followmoney.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.activities.bankAccounts.BankAccountListActivity;
import br.com.followmoney.activities.bankAccounts.BankAccountTransfer;
import br.com.followmoney.activities.creditCards.CreditCardListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.activities.movements.MovementCreateOrEditActivity;
import br.com.followmoney.activities.movements.MovementListActivity;
import br.com.followmoney.activities.paymentForms.PaymentFormListActivity;
import br.com.followmoney.dao.remote.StringValueRequest;
import br.com.followmoney.globals.GlobalParams;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView saldoMesTextView, saldoAnteriorTextView, saldoPrevistoTextView;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //saldoMesTextView = (TextView) findViewById(R.id.saldoMesTextView);
        saldoPrevistoTextView = (TextView) findViewById(R.id.saldoPrevistoTextView);
        saldoAnteriorTextView = (TextView) findViewById(R.id.saldoAnteriorTextView);

        loadBalances();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadBalances();
    }

    protected void loadBalances() {
        //GET PREVIOUS BALANCE
        String context = "/movements/previousBalance" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                saldoAnteriorTextView.setText(numberFormat.format(Double.parseDouble(result)));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);


        //GET FORESEEN BALANCE
        context = "/movements/previousBalance" +
                "/user/"+ GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getNextMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                saldoPrevistoTextView.setText(numberFormat.format(Double.parseDouble(result)));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);

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
        } else if (id == R.id.payment_form) {
            Intent intent = new Intent(this, PaymentFormListActivity.class);
            startActivity(intent);
        } else if (id == R.id.movements) {
            Intent intent = new Intent(this, MovementListActivity.class);
            startActivity(intent);
        } else if (id == R.id.account_transfer) {
            Intent intent = new Intent(this, BankAccountTransfer.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
