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
import br.com.followmoney.activities.movements.MovementDetailActivity;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.util.DateUtil;

import static br.com.followmoney.activities.KeyParams.KEY_BANK_ACCOUNT;
import static br.com.followmoney.activities.KeyParams.KEY_EMISSION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_VALUE;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;
import static br.com.followmoney.activities.KeyParams.KEY_FINALITY;
import static br.com.followmoney.activities.KeyParams.KEY_MATURITY;
import static br.com.followmoney.activities.KeyParams.KEY_VALUE;

public class CreditCardInvoiceMovementListActivity extends AbstractFormList<Movement> {

    int invoiceID, creditCardID;
    String creditCardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_credit_card_invoice_movement_list);
        //params used to on click back
        creditCardID              = getIntent().getIntExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
        creditCardDescription     = getIntent().getStringExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION);

        invoiceID                 = getIntent().getIntExtra(KEY_EXTRA_INVOICE_ID, 0);
        String invoiceDescription = getIntent().getStringExtra(KEY_EXTRA_INVOICE_DESCRIPTION);
        String invoiceValue       = getIntent().getStringExtra(KEY_EXTRA_INVOICE_VALUE);

        TextView invoiceDescriptionTextView = (TextView) findViewById(R.id.invoiceDescriptionTextView);
        invoiceDescriptionTextView.setText(invoiceDescription);

        TextView invoiceValueTextView = (TextView) findViewById(R.id.invoiceValueTextView);
        invoiceValueTextView.setText(invoiceValue);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        for (Movement movement : movements) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(movement.getId()));
            map.put(KEY_DESCRIPTION, movement.getDescricao());
            map.put(KEY_FINALITY, movement.getFinalidade().getDescricao());
            map.put(KEY_EMISSION, "E: " + DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_MATURITY, "V: " + DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_VALUE, "R$ " + String.valueOf(movement.getValor()));
            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.invoice_movement_list_renderer,
                new String[] { KEY_DESCRIPTION, KEY_FINALITY, KEY_VALUE, KEY_BANK_ACCOUNT, KEY_EMISSION, KEY_MATURITY },
                new int[] { R.id.description, R.id.finality, R.id.value, R.id.bank_account, R.id.emission, R.id.maturity});

        listView.setAdapter(adapter);
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
        Intent intent = new Intent(CreditCardInvoiceMovementListActivity.this, MovementDetailActivity.class);
        intent.putExtra(KEY_EXTRA_MOVEMENT_ID, Integer.parseInt(mapList.get(i).get(KEY_ID)));
        startActivity(intent);
    }
}
