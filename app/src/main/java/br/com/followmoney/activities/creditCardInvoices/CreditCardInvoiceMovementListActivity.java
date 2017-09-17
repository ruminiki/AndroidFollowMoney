package br.com.followmoney.activities.creditCardInvoices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.creditCardInvoices.GetCreditCardInvoiceMovements;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.util.DateUtil;

public class CreditCardInvoiceMovementListActivity extends AppCompatActivity {

    private ListView listView;

    private List<HashMap<String, String>> mapList   = new ArrayList<>();
    private static final String KEY_ID              = "id";
    private static final String KEY_DESCRIPTION     = "description";
    private static final String KEY_FINALITY        = "finality";
    private static final String KEY_BANK_ACCOUNT    = "bankAccount";
    private static final String KEY_EMISSION        = "emission";
    private static final String KEY_MATURITY        = "maturity";
    private static final String KEY_VALUE           = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_invoice_movement_list);

        int invoiceID             = getIntent().getIntExtra(CreditCardInvoiceListActivity.KEY_EXTRA_INVOICE_ID, 0);
        String invoiceDescription = getIntent().getStringExtra(CreditCardInvoiceListActivity.KEY_EXTRA_INVOICE_DESCRIPTION);
        String invoiceValue       = getIntent().getStringExtra(CreditCardInvoiceListActivity.KEY_EXTRA_INVOICE_VALUE);

        listView = (ListView) findViewById(R.id.listView);

        TextView invoiceDescriptionTextView = (TextView) findViewById(R.id.invoiceDescriptionTextView);
        invoiceDescriptionTextView.setText(invoiceDescription);

        TextView invoiceValueTextView = (TextView) findViewById(R.id.invoiceValueTextView);
        invoiceValueTextView.setText(invoiceValue);

        new GetCreditCardInvoiceMovements(new GetCreditCardInvoiceMovements.OnLoadListener() {
            @Override
            public void onLoaded(List<Movement> movements) {
                for (Movement movement : movements) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_ID, String.valueOf(movement.getId()));
                    map.put(KEY_DESCRIPTION, movement.getDescricao());
                    map.put(KEY_FINALITY, "Finalidade: " + movement.getFinalidade().getDescricao());

                    if ( movement.getContaBancaria() != null && movement.getContaBancaria().getDescricao() != null ){
                        map.put(KEY_BANK_ACCOUNT, "Conta Bancária: " + movement.getContaBancaria().getDescricao());
                    }else{
                        if ( movement.getCartaoCredito() != null && movement.getCartaoCredito().getDescricao() != null ){
                            map.put(KEY_BANK_ACCOUNT, "Cartão Crédito: " + movement.getCartaoCredito().getDescricao());
                        }
                    }

                    map.put(KEY_EMISSION, "E: " + DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
                    map.put(KEY_MATURITY, "V: " + DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
                    map.put(KEY_VALUE, "R$ " + String.valueOf(movement.getValor()));

                    mapList.add(map);
                }

                ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.movement_list_renderer,
                        new String[] { KEY_DESCRIPTION, KEY_FINALITY, KEY_VALUE, KEY_BANK_ACCOUNT, KEY_EMISSION, KEY_MATURITY },
                        new int[] { R.id.description, R.id.finality, R.id.value, R.id.bank_account, R.id.emission, R.id.maturity});

                listView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(invoiceID);

    }

}
