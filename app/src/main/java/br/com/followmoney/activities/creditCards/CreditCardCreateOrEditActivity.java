package br.com.followmoney.activities.creditCards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.creditCards.GetCreditCard;
import br.com.followmoney.dao.remote.creditCards.PostCreditCard;
import br.com.followmoney.dao.remote.creditCards.PutCreditCard;
import br.com.followmoney.domain.CreditCard;

public class CreditCardCreateOrEditActivity extends AppCompatActivity{

    public final static String KEY_EXTRA_CREDIT_CARD_DESCRIPTION = "KEY_EXTRA_CREDIT_CARD_DESCRIPTION";

    EditText descricaoEditText, limiteEditText, dataFechamentoFaturaEditText, dataVencimentoFaturaEditText;
    ImageButton saveButton;

    int creditCardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_create_or_edit);

        //====BIND TEXT FIELDS====//
        creditCardID                 = getIntent().getIntExtra(CreditCardListActivity.KEY_EXTRA_CREDIT_CARD_ID, 0);
        descricaoEditText            = (EditText) findViewById(R.id.descricaoEditText);
        limiteEditText               = (EditText) findViewById(R.id.limiteEditText);
        dataFechamentoFaturaEditText = (EditText) findViewById(R.id.dataFechamentoFaturaEditText);
        dataVencimentoFaturaEditText = (EditText) findViewById(R.id.dataVencimentoFaturaEditText);

        //====BIND BUTTONS====//
        saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persist();
            }
        });

        if (creditCardID > 0) {
            new GetCreditCard(new GetCreditCard.OnLoadListener() {
                @Override
                public void onLoaded(final CreditCard creditCard) {

                    descricaoEditText.setText(creditCard.getDescricao());
                    descricaoEditText.setEnabled(true);
                    descricaoEditText.setClickable(true);

                    limiteEditText.setText(String.valueOf(creditCard.getLimite()));
                    limiteEditText.setEnabled(true);
                    limiteEditText.setClickable(true);

                    dataFechamentoFaturaEditText.setText(String.valueOf(creditCard.getDataFechamento()));
                    dataFechamentoFaturaEditText.setEnabled(true);
                    dataFechamentoFaturaEditText.setClickable(true);

                    dataVencimentoFaturaEditText.setText(String.valueOf(creditCard.getDataFatura()));
                    dataVencimentoFaturaEditText.setEnabled(true);
                    dataVencimentoFaturaEditText.setClickable(true);

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(creditCardID);

        }
    }

    public void persist() {
        if(creditCardID > 0) {
            new PutCreditCard(new PutCreditCard.OnLoadListener() {
                @Override
                public void onLoaded(CreditCard creditCard) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CreditCardListActivity.class);
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
            new PostCreditCard(new PostCreditCard.OnLoadListener() {
                @Override
                public void onLoaded(CreditCard creditCard) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CreditCardListActivity.class);
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

    private CreditCard getValueDataFieldsInView(){
        CreditCard c = new CreditCard();
        c.setId(creditCardID);
        c.setDescricao(descricaoEditText.getText().toString());
        c.setLimite(Float.parseFloat(limiteEditText.getText().toString()));
        c.setDataFatura(Integer.parseInt(dataVencimentoFaturaEditText.getText().toString()));
        c.setDataFechamento(Integer.parseInt(dataFechamentoFaturaEditText.getText().toString()));
        c.setUsuario(3);
        return c;
    }

}
