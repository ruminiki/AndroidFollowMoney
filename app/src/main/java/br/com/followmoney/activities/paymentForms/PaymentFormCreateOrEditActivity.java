package br.com.followmoney.activities.paymentForms;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.domain.PaymentForm;

public class PaymentFormCreateOrEditActivity extends AbstractFormCreateOrEdit<PaymentForm> {

    EditText editTextDescricao, editTextSigla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_payment_form_create_or_edit);

        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        editTextSigla = (EditText) findViewById(R.id.editTextSigla);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityToEditLoaded(PaymentForm paymentForm) {
        if ( paymentForm != null ){
            editTextDescricao.setText(paymentForm.getDescricao());
            editTextDescricao.setEnabled(true);
            editTextDescricao.setFocusableInTouchMode(true);
            editTextDescricao.setClickable(true);

            editTextSigla.setText(paymentForm.getSigla());
            editTextSigla.setEnabled(true);
            editTextSigla.setClickable(true);
        }
    }

    @Override
    protected PaymentForm getValueDataFieldsInView(Integer id) {
        PaymentForm f = new PaymentForm();
        f.setId(id);
        f.setDescricao(editTextDescricao.getText().toString());
        f.setSigla(editTextSigla.getText().toString());
        f.setUsuario(3);
        return f;
    }

    @Override
    protected String getRestContextGetOrPut() {
        return "/paymentForms/"+entityID;
    }

    @Override
    protected String getRestContextPost() {
        return "/paymentForms";
    }

    @Override
    protected Class getActivityClassList() {
        return PaymentFormListActivity.class;
    }

    @Override
    protected Type getType() {
        return new TypeToken<PaymentForm>(){}.getType();
    }

}
