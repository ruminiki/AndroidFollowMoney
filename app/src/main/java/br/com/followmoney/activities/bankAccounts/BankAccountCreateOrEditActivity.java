package br.com.followmoney.activities.bankAccounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.bankAccounts.GetBankAccount;
import br.com.followmoney.dao.remote.bankAccounts.PostBankAccount;
import br.com.followmoney.dao.remote.bankAccounts.PutBankAccount;
import br.com.followmoney.dao.remote.creditCards.DeleteCreditCard;
import br.com.followmoney.domain.BankAccount;

public class BankAccountCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText descricaoEditText, numeroEditText, digitoEditText;
    Spinner situacaoSpinner;
    Button saveButton, deleteButton;

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
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (bankAccountID > 0) {
            new GetBankAccount(new GetBankAccount.OnLoadListener() {
                @Override
                public void onLoaded(BankAccount bankAccount) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

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

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
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
            public void onLoaded(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BankAccountListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(bankAccountID);
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
