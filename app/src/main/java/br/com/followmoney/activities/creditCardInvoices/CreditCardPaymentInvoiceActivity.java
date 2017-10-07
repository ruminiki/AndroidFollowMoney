package br.com.followmoney.activities.creditCardInvoices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import br.com.followmoney.R;
import br.com.followmoney.activities.bankAccounts.BankAccountListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.activities.paymentForms.PaymentFormListActivity;
import br.com.followmoney.dao.remote.GetEntityJson;
import br.com.followmoney.dao.remote.PutEntityJson;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.CreditCardInvoice;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.domain.PaymentForm;

import static br.com.followmoney.activities.AbstractFormList.KEY_DESCRIPTION;
import static br.com.followmoney.activities.AbstractFormList.KEY_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_INVOICE_ID;

/**
 * Created by ruminiki on 07/10/2017.
 */

public class CreditCardPaymentInvoiceActivity extends AppCompatActivity {

    private static final int KEY_SELECT_FORMA_PAGAMENTO_RETURN = 0;
    private static final int KEY_SELECT_FINALIDADE_RETURN      = 1;
    private static final int KEY_SELECT_CONTA_BANCARIA_RETURN  = 3;

    EditText finalidadeEditText, formaPagamentoEditText, contaBancariaEditText;
    TextView invoiceDescriptionTextView, invoiceValueTextView, invoiceLimitAvailableTextView;

    private CreditCardInvoice creditCardInvoice;
    private Finality finalidade;
    private PaymentForm formaPagamento;
    private BankAccount contaBancaria;

    int invoiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment_invoice);

        invoiceID = getIntent().getIntExtra(KEY_EXTRA_INVOICE_ID, 0);

        invoiceDescriptionTextView = (TextView) findViewById(R.id.invoiceDescriptionTextView);
        invoiceValueTextView = (TextView) findViewById(R.id.invoiceValueTextView);
        invoiceLimitAvailableTextView = (TextView) findViewById(R.id.invoiceLimitAvailableTextView);

        ImageButton payInvoiceButton = (ImageButton) findViewById(R.id.payInvoiceButton);
        if ( payInvoiceButton != null ) {
            payInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payInvoice();
                }
            });
        }

        finalidadeEditText = (EditText) findViewById(R.id.finalidadeEditText);
        finalidadeEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFinalidadeFormToSelect();
            }
        });

        formaPagamentoEditText = (EditText) findViewById(R.id.formaPagamentoEditText);
        formaPagamentoEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFormaPagamentoFormToSelect();
            }
        });

        contaBancariaEditText = (EditText) findViewById(R.id.contaBancariaEditText);
        contaBancariaEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openContaBancariaFormToSelect();
            }
        });

        loadInvoice();
    }

    private void loadInvoice(){
        if (invoiceID > 0) {
            new GetEntityJson<CreditCardInvoice>(new GetEntityJson.OnLoadListener<CreditCardInvoice>() {
                @Override
                public void onLoaded(CreditCardInvoice entity) {
                    creditCardInvoice = entity;
                    invoiceDescriptionTextView.setText(
                            creditCardInvoice.getCartaoCredito().getDescricao() + " " +
                            creditCardInvoice.getMesReferencia().toUpperCase() + " " +
                            creditCardInvoice.getStatus().toUpperCase()
                    );
                    invoiceValueTextView.setText("R$" + creditCardInvoice.getValor());
                    invoiceLimitAvailableTextView.setText("R$ 0,00");//@TODO buscar valor limite disponível
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }, this).execute("/creditCardInvoices/"+invoiceID, new TypeToken<CreditCardInvoice>(){}.getType());

        }
    }

    private void payInvoice(){
        new PutEntityJson<CreditCardInvoice>(new PutEntityJson.OnLoadListener<CreditCardInvoice>() {
            @Override
            public void onLoaded(CreditCardInvoice t) {
                Toast.makeText(getApplicationContext(), "Invoice paid successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }, this).execute(getValueDataFieldsInView(), "/creditCardInvoices/pay/"+invoiceID, new TypeToken<CreditCardInvoice>(){}.getType());
    }

    ///=====SELECT FINALIDADE======//
    private void openFinalidadeFormToSelect(){
        Intent intent = new Intent(this, FinalityListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(FinalityListActivity.KEY_MODE, FinalityListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FINALIDADE_RETURN);
    }

    ///=====SELECT FORMA PAGAMENTO ======//
    private void openFormaPagamentoFormToSelect(){
        Intent intent = new Intent(this, PaymentFormListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(PaymentFormListActivity.KEY_MODE, PaymentFormListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FORMA_PAGAMENTO_RETURN);
    }

    ///=====START: POPULATE CONTA BANCARIA SPINNER======//
    private void openContaBancariaFormToSelect(){
        Intent intent = new Intent(this, BankAccountListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(BankAccountListActivity.KEY_MODE, BankAccountListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_CONTA_BANCARIA_RETURN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case KEY_SELECT_FORMA_PAGAMENTO_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra(KEY_ID, 0);
                    String descricao = data.getStringExtra(KEY_DESCRIPTION);
                    if ( formaPagamento == null ) {
                        formaPagamento = new PaymentForm(id, descricao);
                    }else{
                        formaPagamento.setDescricao(descricao);
                        formaPagamento.setId(id);
                    }
                    formaPagamentoEditText.setText(descricao);
                }
                break;
            }

            case KEY_SELECT_FINALIDADE_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id           = data.getIntExtra(KEY_ID, 0);
                    String descricao = data.getStringExtra(KEY_DESCRIPTION);
                    if ( finalidade == null ) {
                        finalidade = new Finality(id, descricao);
                    }else{
                        finalidade.setDescricao(descricao);
                        finalidade.setId(id);
                    }
                    finalidadeEditText.setText(descricao);
                }
                break;
            }

            case KEY_SELECT_CONTA_BANCARIA_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id           = data.getIntExtra(KEY_ID, 0);
                    String descricao = data.getStringExtra(KEY_DESCRIPTION);
                    if ( contaBancaria == null ) {
                        contaBancaria = new BankAccount(id, descricao);
                    }else{
                        contaBancaria.setDescricao(descricao);
                        contaBancaria.setId(id);
                    }
                    contaBancariaEditText.setText(descricao);
                }
                break;
            }
        }
    }

    protected CreditCardInvoice getValueDataFieldsInView() {
        creditCardInvoice.setFormaPagamento(formaPagamento);
        creditCardInvoice.setContaBancaria(contaBancaria);
        creditCardInvoice.setValorPagamento(creditCardInvoice.getValor());//@TODO set valo pagamento menor que fatura
        return creditCardInvoice;
    }

}
