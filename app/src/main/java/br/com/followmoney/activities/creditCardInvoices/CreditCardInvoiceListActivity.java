package br.com.followmoney.activities.creditCardInvoices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.domain.CreditCardInvoice;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_VALUE;

public class CreditCardInvoiceListActivity extends AbstractFormList<CreditCardInvoice>{

    int creditCardID;
    String creditCardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_credit_card_invoice_list);

        creditCardID          = getIntent().getIntExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
        creditCardDescription = getIntent().getStringExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION);

        TextView creditCardDescriptionTextView = (TextView) findViewById(R.id.creditCardDescriptionTextView);
        creditCardDescriptionTextView.setText(creditCardDescription);

        super.onCreate(savedInstanceState);

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
        selectedEntity = (CreditCardInvoice) listView.getItemAtPosition(i);

        Intent intent = new Intent(CreditCardInvoiceListActivity.this, CreditCardInvoiceMovementListActivity.class);
        intent.putExtra(KEY_EXTRA_INVOICE_ID, selectedEntity.getId());

        intent.putExtra(KEY_EXTRA_INVOICE_DESCRIPTION, creditCardDescription.toUpperCase() +
                        " " + selectedEntity.getMesReferencia().toUpperCase() +
                        " (" + selectedEntity.getStatus().toUpperCase()+")");

        intent.putExtra(KEY_EXTRA_INVOICE_VALUE, "R$ " + selectedEntity.getValor());

        intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, creditCardID);
        intent.putExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION, creditCardDescription);

        startActivity(intent);
    }

}
