package br.com.followmoney.activities.creditCardInvoices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.CreditCardInvoice;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_VALUE;
import static br.com.followmoney.activities.KeyParams.KEY_REFERENCE_MONTH;
import static br.com.followmoney.activities.KeyParams.KEY_STATUS;
import static br.com.followmoney.activities.KeyParams.KEY_VALUE;

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
        for (CreditCardInvoice creditCardInvoice : creditCardInvoices) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(creditCardInvoice.getId()));
            map.put(KEY_VALUE, "R$ " + creditCardInvoice.getValor());
            map.put(KEY_REFERENCE_MONTH, creditCardInvoice.getMesReferencia().toUpperCase());
            map.put(KEY_STATUS, creditCardInvoice.getStatus());

            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(CreditCardInvoiceListActivity.this, mapList, R.layout.credit_card_invoice_list_renderer,
                new String[] { KEY_REFERENCE_MONTH, KEY_VALUE, KEY_STATUS },
                new int[] { R.id.referenceMonth, R.id.value, R.id.status});

        listView.setAdapter(adapter);
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
        Intent intent = new Intent(CreditCardInvoiceListActivity.this, CreditCardInvoiceMovementListActivity.class);
        intent.putExtra(KEY_EXTRA_INVOICE_ID, Integer.parseInt(mapList.get(i).get(KEY_ID)));

        intent.putExtra(KEY_EXTRA_INVOICE_DESCRIPTION, creditCardDescription.toUpperCase() +
                        " " + mapList.get(i).get(KEY_REFERENCE_MONTH).toUpperCase() +
                        " (" + mapList.get(i).get(KEY_STATUS).toUpperCase()+")");
        intent.putExtra(KEY_EXTRA_INVOICE_VALUE, mapList.get(i).get(KEY_VALUE));

        intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, creditCardID);
        intent.putExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION, creditCardDescription);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                super.loadList();
            }
    }
}
