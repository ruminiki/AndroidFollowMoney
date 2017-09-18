package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.bankAccounts.GetBankAccount;
import br.com.followmoney.dao.remote.bankAccounts.PostBankAccount;
import br.com.followmoney.dao.remote.bankAccounts.PutBankAccount;
import br.com.followmoney.domain.BankAccount;

public class BankAccountCreateOrEditActivity extends AppCompatActivity{

    EditText descricaoEditText, numeroEditText, digitoEditText;
    Spinner situacaoSpinner;
    ImageButton saveButton;

    int bankAccountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_create_or_edit);

        //====BIND TEXT FIELDS====//
        bankAccountID     = getIntent().getIntExtra(BankAccountListActivity.KEY_EXTRA_BANK_ACCOUNT_ID, 0);
        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        numeroEditText    = (EditText) findViewById(R.id.numeroEditText);
        situacaoSpinner   = (Spinner)  findViewById(R.id.situacaoSpinner);
        digitoEditText    = (EditText) findViewById(R.id.digitoEditText);

        //====BIND BUTTONS====//
        saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            persist();
            }
        });

        if (bankAccountID > 0) {
            new GetBankAccount(new GetBankAccount.OnLoadListener() {
                @Override
                public void onLoaded(BankAccount bankAccount) {

                    descricaoEditText.setText(bankAccount.getDescricao());
                    descricaoEditText.setEnabled(true);
                    descricaoEditText.setClickable(true);

                    numeroEditText.setText(String.valueOf(bankAccount.getNumero()));
                    numeroEditText.setEnabled(true);
                    numeroEditText.setClickable(true);

                    situacaoSpinner.setSelection( ((ArrayAdapter) situacaoSpinner.getAdapter()).getPosition(bankAccount.getSituacao()));
                    situacaoSpinner.setEnabled(true);
                    situacaoSpinner.setClickable(true);

                    digitoEditText.setText(String.valueOf(bankAccount.getDigito()));
                    digitoEditText.setEnabled(true);
                    digitoEditText.setClickable(true);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(bankAccountID);

        }
    }

    public void persist() {
        if(bankAccountID > 0) {
            new PutBankAccount(new PutBankAccount.OnLoadListener() {
                @Override
                public void onLoaded(BankAccount bankAccount) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), BankAccountListActivity.class);
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
            new PostBankAccount(new PostBankAccount.OnLoadListener() {
                @Override
                public void onLoaded(BankAccount bankAccount) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), BankAccountListActivity.class);
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

    private BankAccount getValueDataFieldsInView(){
        BankAccount b = new BankAccount();
        b.setId(bankAccountID);
        b.setDescricao(descricaoEditText.getText().toString());
        b.setNumero(numeroEditText.getText().toString());
        b.setDigito(Integer.parseInt(digitoEditText.getText().toString()));
        b.setSituacao(situacaoSpinner.getSelectedItem().toString());
        b.setUsuario(3);
        return b;
    }

}
