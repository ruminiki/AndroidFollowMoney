package br.com.followmoney.activities.creditCardInvoices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.creditCards.CreditCardCreateOrEditActivity;
import br.com.followmoney.dao.remote.creditCardInvoices.GetCreditCardInvoices;
import br.com.followmoney.domain.CreditCardInvoice;

import static br.com.followmoney.activities.SelectableActivity.KEY_ID;
import static br.com.followmoney.activities.creditCards.CreditCardListActivity.KEY_EXTRA_CREDIT_CARD_ID;

public class CreditCardInvoiceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_INVOICE_ID          = "KEY_EXTRA_INVOICE_ID";
    public final static String KEY_EXTRA_INVOICE_DESCRIPTION = "KEY_EXTRA_INVOICE_DESCRIPTION";
    public final static String KEY_EXTRA_INVOICE_VALUE       = "KEY_EXTRA_INVOICE_VALUE";

    private ListView listView;

    private List<HashMap<String, String>> mapList   = new ArrayList<>();
    private static final String KEY_VALUE           = "value";
    private static final String KEY_REFERENCE_MONTH = "reference_month";
    private static final String KEY_STATUS          = "status";

    int creditCardID;
    String creditCardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_invoice_list);

        creditCardID          = getIntent().getIntExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
        creditCardDescription = getIntent().getStringExtra(CreditCardCreateOrEditActivity.KEY_EXTRA_CREDIT_CARD_DESCRIPTION);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        TextView creditCardDescriptionTextView = (TextView) findViewById(R.id.creditCardDescriptionTextView);
        creditCardDescriptionTextView.setText(creditCardDescription);

        new GetCreditCardInvoices(new GetCreditCardInvoices.OnLoadListener() {
            @Override
            public void onLoaded(List<CreditCardInvoice> creditCardInvoices) {
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
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(creditCardID);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), CreditCardInvoiceMovementListActivity.class);
        intent.putExtra(KEY_EXTRA_INVOICE_ID, Integer.parseInt(mapList.get(i).get(KEY_ID)));
        intent.putExtra(KEY_EXTRA_INVOICE_DESCRIPTION, creditCardDescription.toUpperCase() +
                        " " + mapList.get(i).get(KEY_REFERENCE_MONTH).toUpperCase() +
                        " (" + mapList.get(i).get(KEY_STATUS).toUpperCase()+")");
        intent.putExtra(KEY_EXTRA_INVOICE_VALUE, mapList.get(i).get(KEY_VALUE));
        startActivity(intent);
    }

}
