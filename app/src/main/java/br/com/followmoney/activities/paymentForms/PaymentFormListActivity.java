package br.com.followmoney.activities.paymentForms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.domain.PaymentForm;

public class PaymentFormListActivity extends AbstractFormList<PaymentForm> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_payment_form_list);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityListLoaded(List<PaymentForm> paymentForms) {
        listView.setAdapter(new CustomListAdapter<PaymentForm>(this, R.layout.payment_form_list_renderer, paymentForms));
    }

    @Override
    protected String getRestContextList() {
        return "/paymentForms/user/3"; //@TODO get user logged in
    }

    @Override
    protected String getRestContextDelete() {
        return "/paymentForms/"+selectedEntityID;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {
        Intent intent = new Intent(PaymentFormListActivity.this, PaymentFormCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<PaymentForm>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntity = (PaymentForm) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, selectedEntity.getId());
            intent.putExtra(KEY_DESCRIPTION, selectedEntity.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
