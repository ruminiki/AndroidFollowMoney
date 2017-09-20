package br.com.followmoney.activities.paymentForms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.PaymentForm;

public class PaymentFormListActivity extends AbstractFormList<PaymentForm> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_payment_form_list);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityListLoaded(List<PaymentForm> paymentForms) {
        for (PaymentForm paymentForm : paymentForms) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(paymentForm.getId()));
            map.put(KEY_DESCRIPTION, paymentForm.getDescricao());

            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(PaymentFormListActivity.this, mapList, R.layout.payment_form_list_renderer,
                new String[] { KEY_DESCRIPTION },
                new int[] { R.id.description});

        listView.setAdapter(adapter);
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
        selectedEntityID = Integer.parseInt(mapList.get(i).get(KEY_ID));
        if ( MODE == OPEN_TO_SELECT_MODE ){
            PaymentForm f = (PaymentForm) listView.getSelectedItem();
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, f.getId());
            intent.putExtra(KEY_DESCRIPTION, f.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
