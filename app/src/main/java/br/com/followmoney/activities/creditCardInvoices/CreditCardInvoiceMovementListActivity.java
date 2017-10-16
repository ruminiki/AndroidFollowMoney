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
import br.com.followmoney.activities.movements.MovementDetailActivity;
import br.com.followmoney.domain.Movement;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_MONTH_REFERENCE;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_STATUS;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_VALUE;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;

public class CreditCardInvoiceMovementListActivity extends AbstractFormList<Movement> {

    int invoiceID, creditCardID;
    String creditCardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_credit_card_invoice_movement_list);
        //params used to on click back
        creditCardID              = getIntent().getIntExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
        invoiceID                 = getIntent().getIntExtra(KEY_EXTRA_INVOICE_ID, 0);

        TextView mesReferenciaTextView = (TextView) findViewById(R.id.mesReferenciaTextView);
        mesReferenciaTextView.setText(getIntent().getStringExtra(KEY_EXTRA_INVOICE_MONTH_REFERENCE));

        TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setText(getIntent().getStringExtra(KEY_EXTRA_INVOICE_STATUS));

        TextView creditCardTextView = (TextView) findViewById(R.id.creditCardTextView);
        creditCardTextView.setText(getIntent().getStringExtra(KEY_EXTRA_INVOICE_CREDIT_CARD_DESCRIPTION));

        TextView invoiceValueTextView = (TextView) findViewById(R.id.invoiceValueTextView);
        invoiceValueTextView.setText(getIntent().getStringExtra(KEY_EXTRA_INVOICE_VALUE));

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        listView.setAdapter(new CustomListAdapter<Movement>(this, R.layout.invoice_movement_list_renderer, movements));
    }

    @Override
    protected String getRestContextList() {
        return "/movements/invoice/"+invoiceID;
    }

    @Override
    protected String getRestContextDelete() { return null; }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {  }

    @Override
    protected Type getType() {
        return new TypeToken<List<Movement>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntity = (Movement) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        Intent intent = new Intent(CreditCardInvoiceMovementListActivity.this, MovementDetailActivity.class);
        intent.putExtra(KEY_EXTRA_MOVEMENT_ID, selectedEntity.getId());
        startActivity(intent);
    }
}
