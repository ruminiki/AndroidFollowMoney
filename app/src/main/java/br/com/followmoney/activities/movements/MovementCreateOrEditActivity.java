package br.com.followmoney.activities.movements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.components.EditTextDatePicker;
import br.com.followmoney.dao.remote.bankAccounts.GetBankAccounts;
import br.com.followmoney.dao.remote.creditCards.GetCreditCards;
import br.com.followmoney.dao.remote.finalities.DeleteFinality;
import br.com.followmoney.dao.remote.finalities.GetFinalities;
import br.com.followmoney.dao.remote.movements.GetMovement;
import br.com.followmoney.dao.remote.movements.PostMovement;
import br.com.followmoney.dao.remote.movements.PutMovement;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.util.DateUtil;

public class MovementCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText descricaoEditText, emissaoEditText, vencimentoEditText;
    RadioButton creditoRadio, debitoRadio;
    Spinner contaBancariaSpinner, finalidadeSpinner, formaPagamentoSpinner, cartaoCreditoSpinner;
    Button saveButton, deleteButton;

    int movimentoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_create_or_edit);

        movimentoID = getIntent().getIntExtra(MovementListActivity.KEY_EXTRA_MOVEMENT_ID, 0);
        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        emissaoEditText = (EditText) findViewById(R.id.emissaoEditText);
        new EditTextDatePicker(this, emissaoEditText);

        vencimentoEditText = (EditText) findViewById(R.id.vencimentoEditText);
        new EditTextDatePicker(this, vencimentoEditText);

        creditoRadio = (RadioButton) findViewById(R.id.creditoRadio);
        debitoRadio = (RadioButton) findViewById(R.id.debitoRadio);

        saveButton = (Button) findViewById( R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        finalidadeSpinner = (Spinner) findViewById(R.id.finalidadeSpinner);
        populateFinalidadeSpinner();

        contaBancariaSpinner = (Spinner) findViewById(R.id.contaBancariaSpinner);
        populateContaBancariaSpinner();

        cartaoCreditoSpinner = (Spinner) findViewById(R.id.cartaoCreditoSpinner);
        populateCartaoCreditoSpinner();

        if (movimentoID > 0) {
            new GetMovement(new GetMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    descricaoEditText.setText(movement.getDescricao());
                    descricaoEditText.setEnabled(true);
                    descricaoEditText.setFocusableInTouchMode(true);
                    descricaoEditText.setClickable(true);

                    emissaoEditText.setText(DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
                    emissaoEditText.setClickable(true);

                    vencimentoEditText.setText(DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
                    vencimentoEditText.setClickable(true);

                    creditoRadio.setChecked(movement.getOperacao().equals(Movement.CREDIT));
                    debitoRadio.setChecked(movement.getOperacao().equals(Movement.DEBIT));
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(movimentoID);

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
            debitoRadio.setChecked(true);
        }
    }

    ///=====START: POPULATE FINALIDADE SPINNER======//
    private void populateFinalidadeSpinner(){
        new GetFinalities(new GetFinalities.OnLoadListener() {
            @Override
            public void onLoaded(List<Finality> finalities) {
                populateFinalidadeSpinner(finalities);
            }
            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(3);
    }

    private void populateFinalidadeSpinner(List<Finality> finalities){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner, finalities);
        finalidadeSpinner.setAdapter(adapter);
    }

    ///=====START: POPULATE CONTA BANCARIA SPINNER======//
    private void populateContaBancariaSpinner(){
        new GetBankAccounts(new GetBankAccounts.OnLoadListener() {
            @Override
            public void onLoaded(List<BankAccount> bankAccounts) {
                populateContaBancariaSpinner(bankAccounts);
            }
            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(3);
    }

    private void populateContaBancariaSpinner(List<BankAccount> bankAccounts){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner, bankAccounts);
        contaBancariaSpinner.setAdapter(adapter);
    }

    ///=====START: POPULATE CARTAO CREDITO SPINNER======//
    private void populateCartaoCreditoSpinner(){
        new GetCreditCards(new GetCreditCards.OnLoadListener() {
            @Override
            public void onLoaded(List<CreditCard> creditCards) {
                populateCartaoCreditoSpinner(creditCards);
            }
            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(3);
    }

    private void populateCartaoCreditoSpinner(List<CreditCard> creditCards){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner, creditCards);
        cartaoCreditoSpinner.setAdapter(adapter);
    }
    ///=====END: POPULATE CARTAO CREDITO SPINNER======//

    public void persist() {
        if(movimentoID > 0) {
            new PutMovement(new PutMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
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
            new PostMovement(new PostMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
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
        new DeleteFinality(new DeleteFinality.OnLoadListener() {
            @Override
            public void onLoaded(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(movimentoID);
    }

    private Movement getValueDataFieldsInView(){
        Movement m = new Movement();
        m.setId(movimentoID);
        m.setDescricao(descricaoEditText.getText().toString());
        m.setEmissao(DateUtil.format(emissaoEditText.getText().toString(), "dd/MM/yyyy", "yyyyMMdd"));
        m.setVencimento(DateUtil.format(vencimentoEditText.getText().toString(), "dd/MM/yyyy", "yyyyMMdd"));
        m.setOperacao(creditoRadio.isChecked() ? Movement.CREDIT : Movement.DEBIT);
        m.setUsuario(3);
        return m;
    }

}
