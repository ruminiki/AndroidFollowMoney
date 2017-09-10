package br.com.followmoney.activities.creditCards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.creditCards.DeleteCreditCard;
import br.com.followmoney.dao.remote.creditCards.GetCreditCard;
import br.com.followmoney.dao.remote.creditCards.PostCreditCard;
import br.com.followmoney.dao.remote.creditCards.PutCreditCard;
import br.com.followmoney.domain.CreditCard;

public class CreditCardCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText descricaoEditText, limiteEditText, dataFechamentoFaturaEditText, dataVencimentoFaturaEditText;
    Button saveButton, deleteButton;

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
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (creditCardID > 0) {
            new GetCreditCard(new GetCreditCard.OnLoadListener() {
                @Override
                public void onLoaded(CreditCard creditCard) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

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

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
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
        new DeleteCreditCard(new DeleteCreditCard.OnLoadListener() {
            @Override
            public void onLoaded(CreditCard creditCard) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CreditCardListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(creditCardID);
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
