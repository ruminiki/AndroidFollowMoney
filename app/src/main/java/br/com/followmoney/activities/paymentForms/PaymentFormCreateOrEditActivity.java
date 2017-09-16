package br.com.followmoney.activities.paymentForms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.paymentForms.DeletePaymentForm;
import br.com.followmoney.dao.remote.paymentForms.GetPaymentForm;
import br.com.followmoney.dao.remote.paymentForms.PostPaymentForm;
import br.com.followmoney.dao.remote.paymentForms.PutPaymentForm;
import br.com.followmoney.domain.PaymentForm;

public class PaymentFormCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextDescricao, editTextSigla;
    ImageButton saveButton, deleteButton;

    int formaPagamentoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form_create_or_edit);

        formaPagamentoID = getIntent().getIntExtra(PaymentFormListActivity.KEY_EXTRA_PAYMENT_FORM_ID, 0);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        editTextSigla = (EditText) findViewById(R.id.editTextSigla);

        saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (formaPagamentoID > 0) {
            new GetPaymentForm(new GetPaymentForm.OnLoadListener() {
                @Override
                public void onLoaded(PaymentForm paymentForm) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    editTextDescricao.setText(paymentForm.getDescricao());
                    editTextDescricao.setEnabled(true);
                    editTextDescricao.setFocusableInTouchMode(true);
                    editTextDescricao.setClickable(true);

                    editTextSigla.setText(paymentForm.getSigla());
                    editTextSigla.setEnabled(true);
                    editTextSigla.setClickable(true);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(formaPagamentoID);

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void persist() {
        if(formaPagamentoID > 0) {
            new PutPaymentForm(new PutPaymentForm.OnLoadListener() {
                @Override
                public void onLoaded(PaymentForm paymentForm) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), PaymentFormListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView());

        }
        else {
            new PostPaymentForm(new PostPaymentForm.OnLoadListener() {
                @Override
                public void onLoaded(PaymentForm paymentForm) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), PaymentFormListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Could not Insert object", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persist();
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                confirmDelete();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Object?");
                d.show();
                return;
        }
    }

    private void confirmDelete(){
        new DeletePaymentForm(new DeletePaymentForm.OnLoadListener() {
            @Override
            public void onLoaded(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PaymentFormListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(formaPagamentoID);
    }

    private PaymentForm getValueDataFieldsInView(){
        PaymentForm f = new PaymentForm();
        f.setId(formaPagamentoID);
        f.setDescricao(editTextDescricao.getText().toString());
        f.setSigla(editTextSigla.getText().toString());
        f.setUsuario(3);
        return f;
    }

}
