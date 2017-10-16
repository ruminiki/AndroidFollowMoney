package br.com.followmoney.activities.creditCardInvoices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.dao.remote.PutEntityJson;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.CreditCardInvoice;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_MONTH_REFERENCE;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_STATUS;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_VALUE;

public class CreditCardInvoiceListActivity extends AbstractFormList<CreditCardInvoice>{

    private static final int KEY_PAYMENT_SUCCESS = 0;

    int creditCardID;
    String creditCardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_credit_card_invoice_list);

        creditCardID          = getIntent().getIntExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
        creditCardDescription = getIntent().getStringExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION);

        TextView creditCardDescriptionTextView = (TextView) findViewById(R.id.creditCardDescriptionTextView);
        creditCardDescriptionTextView.setText(creditCardDescription);

        ImageButton listMovementsInvoiceButton = (ImageButton) findViewById(R.id.listMovementsInvoiceButton);
        if ( listMovementsInvoiceButton != null ) {
            listMovementsInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listMovementsInvoice();
                }
            });
        }

        ImageButton unpayInvoiceButton = (ImageButton) findViewById(R.id.unpayInvoiceButton);
        if ( unpayInvoiceButton != null ) {
            unpayInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
                    builder.setMessage(R.string.unpay_invoice)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    unpayInvoice();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Deseja reabrir a fatura?");
                    d.show();
                    return;
                }
            });
        }

        ImageButton payInvoiceButton = (ImageButton) findViewById(R.id.payInvoiceButton);
        if ( payInvoiceButton != null ) {
            payInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payInvoice();
                }
            });
        }

        super.onCreate(savedInstanceState);
    }

    private void listMovementsInvoice(){
        Intent intent = new Intent(CreditCardInvoiceListActivity.this, CreditCardInvoiceMovementListActivity.class);
        intent.putExtra(KEY_EXTRA_INVOICE_ID, selectedEntity.getId());
        intent.putExtra(KEY_EXTRA_INVOICE_CREDIT_CARD_DESCRIPTION, creditCardDescription.toUpperCase());
        intent.putExtra(KEY_EXTRA_INVOICE_MONTH_REFERENCE, selectedEntity.getMesReferencia().toUpperCase());
        intent.putExtra(KEY_EXTRA_INVOICE_STATUS, selectedEntity.getStatus().toUpperCase());
        intent.putExtra(KEY_EXTRA_INVOICE_VALUE, selectedEntity.getValorFormatado());
        intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, creditCardID);
        startActivity(intent);
    }

    private void payInvoice(){
        if ( selectedEntity != null && !selectedEntity.getStatus().equals(CreditCard.STATUS_CLOSED) ){
            Intent intent = new Intent(CreditCardInvoiceListActivity.this, CreditCardPaymentInvoiceActivity.class);
            intent.putExtra(KEY_EXTRA_INVOICE_ID, selectedEntity.getId());
            startActivityForResult(intent, 0);
        }else{
            Toast.makeText(getApplicationContext(), "A fatura selecionada já está fechada!", Toast.LENGTH_SHORT).show();
        }
    }

    private void unpayInvoice(){
        new PutEntityJson<CreditCardInvoice>(new PutEntityJson.OnLoadListener<CreditCardInvoice>() {
            @Override
            public void onLoaded(CreditCardInvoice t) {
                Toast.makeText(getApplicationContext(), "Invoice payment successfully undone!", Toast.LENGTH_SHORT).show();
                updateObjectInListView(t);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }, this).execute(selectedEntity, "/creditCardInvoices/unpay/"+selectedEntity.getId(), new TypeToken<CreditCardInvoice>(){}.getType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KEY_PAYMENT_SUCCESS: {
                if (resultCode == RESULT_OK) {
                    selectedEntity.setStatus(CreditCard.STATUS_CLOSED);
                    updateObjectInListView(selectedEntity);
                }
                break;
            }
        }
    }
        @Override
    protected void entityListLoaded(List<CreditCardInvoice> creditCardInvoices) {
        listView.setAdapter(new CustomListAdapter<CreditCardInvoice>(this, R.layout.credit_card_invoice_list_renderer, creditCardInvoices));
    }

    @Override
    protected String getRestContextList() {
        return "/creditCardInvoices/creditCard/"+creditCardID;
    }

    @Override
    protected String getRestContextDelete() {
        return "/creditCardInvoices/"+creditCardID;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) { }

    @Override
    protected Type getType() {
        return new TypeToken<List<CreditCardInvoice>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntity   = (CreditCardInvoice) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity.getId();
        selectedEntityPosition = i;
    }

}
