package br.com.followmoney.activities.creditCards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.creditCardInvoices.CreditCardInvoiceListActivity;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.Finality;

public class CreditCardListActivity extends AbstractFormList<CreditCard> {

    private static final String KEY_LIMIT        = "limit";
    private static final String KEY_CLOSING_DATE = "closing_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_credit_card_list);

        //===CARREGA AS FATURAS DO CARTÃO======
        ImageButton invoiceButton = (ImageButton) findViewById(R.id.listInvoicesButton);
        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( selectedEntityPosition >= 0 ) {
                    Intent intent = new Intent(getApplicationContext(), CreditCardInvoiceListActivity.class);
                    intent.putExtra(CreditCardInvoiceListActivity.KEY_EXTRA_CREDIT_CARD_ID, selectedEntityID);
                    intent.putExtra(CreditCardInvoiceListActivity.KEY_EXTRA_CREDIT_CARD_DESCRIPTION, mapList.get(selectedEntityPosition).get(KEY_DESCRIPTION));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Please, you need select an object to show invoices!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityListLoaded(List<CreditCard> creditCards) {
        for (CreditCard creditCard : creditCards) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(creditCard.getId()));
            map.put(KEY_DESCRIPTION, creditCard.getDescricao());
            map.put(KEY_LIMIT, String.valueOf(creditCard.getLimite()));
            map.put(KEY_CLOSING_DATE, String.valueOf(creditCard.getDataFechamento()));

            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(CreditCardListActivity.this, mapList, R.layout.credit_card_list_renderer,
                new String[] { KEY_DESCRIPTION, KEY_LIMIT, KEY_CLOSING_DATE },
                new int[] { R.id.description, R.id.limit, R.id.closing_date});

        listView.setAdapter(adapter);
    }

    @Override
    protected String getRestContextList() {
        return "/creditCards/user/3"; //@TODO get user logged in
    }

    @Override
    protected String getRestContextDelete() {
        return "/creditCards/"+selectedEntityID;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {
        Intent intent = new Intent(CreditCardListActivity.this, CreditCardCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<CreditCard>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntityID = Integer.parseInt(mapList.get(i).get(KEY_ID));
        selectedEntityPosition = i;
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Finality f = (Finality) listView.getSelectedItem();
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, f.getId());
            intent.putExtra(KEY_DESCRIPTION, f.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
