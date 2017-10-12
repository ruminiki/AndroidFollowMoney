package br.com.followmoney.activities.creditCards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.activities.creditCardInvoices.CreditCardInvoiceListActivity;
import br.com.followmoney.domain.CreditCard;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_CREDIT_CARD_ID;
import static br.com.followmoney.activities.KeyParams.KEY_INVOICE_DAY_CLOSING;
import static br.com.followmoney.activities.KeyParams.KEY_INVOICE_DAY_MATURITY;

public class CreditCardListActivity extends AbstractFormList<CreditCard> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_credit_card_list);

        //===CARREGA AS FATURAS DO CARTÃO======
        ImageButton invoiceButton = (ImageButton) findViewById(R.id.listInvoicesButton);
        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( selectedEntity != null ) {
                    Intent intent = new Intent(getApplicationContext(), CreditCardInvoiceListActivity.class);
                    intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, selectedEntity.getId());
                    intent.putExtra(KEY_EXTRA_CREDIT_CARD_DESCRIPTION, selectedEntity.getDescricao());
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
        listView.setAdapter(new CustomListAdapter<CreditCard>(this, R.layout.credit_card_list_renderer, creditCards));
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
        selectedEntity = (CreditCard) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, selectedEntity.getId());
            intent.putExtra(KEY_DESCRIPTION, selectedEntity.getDescricao());
            intent.putExtra(KEY_INVOICE_DAY_CLOSING, selectedEntity.getDataFechamento());
            intent.putExtra(KEY_INVOICE_DAY_MATURITY, selectedEntity.getDataFatura());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
