package br.com.followmoney.activities.movements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import br.com.followmoney.R;
import br.com.followmoney.activities.bankAccounts.BankAccountListActivity;
import br.com.followmoney.activities.creditCards.CreditCardListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.activities.paymentForms.PaymentFormListActivity;
import br.com.followmoney.components.EditTextDatePicker;
import br.com.followmoney.dao.remote.finalities.DeleteFinality;
import br.com.followmoney.dao.remote.movements.GetMovement;
import br.com.followmoney.dao.remote.movements.PostMovement;
import br.com.followmoney.dao.remote.movements.PutMovement;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.domain.PaymentForm;
import br.com.followmoney.util.DateUtil;

public class MovementCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int KEY_SELECT_FORMA_PAGAMENTO_RETURN = 0;
    private static final int KEY_SELECT_FINALIDADE_RETURN      = 1;
    private static final int KEY_SELECT_CARTAO_CREDITO_RETURN  = 2;
    private static final int KEY_SELECT_CONTA_BANCARIA_RETURN  = 3;

    EditText descricaoEditText, emissaoEditText, vencimentoEditText, valorEditText,
             finalidadeEditText, cartaoCreditoEditText, formaPagamentoEditText, contaBancariaEditText;
    ToggleButton toggleButtonCredito, toggleButtonDebito;
    ImageButton saveButton, deleteButton, finalidadeSelectButton, formaPagamentoSelectButton, contaBancariaSelectButton, cartaoCreditoSelectButton;

    int movimentoID;

    private Finality finalidade;
    private PaymentForm formaPagamento;
    private CreditCard cartaoCredito;
    private BankAccount contaBancaria;

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

        toggleButtonCredito = (ToggleButton) findViewById(R.id.toggleButtonCredito);
        toggleButtonCredito.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                toggleButtonDebito.setChecked(false);
                toggleButtonCredito.setChecked(true);
            }
        });

        toggleButtonDebito = (ToggleButton) findViewById(R.id.toggleButtonDebito);
        toggleButtonDebito.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                toggleButtonDebito.setChecked(true);
                toggleButtonCredito.setChecked(false);
            }
        });

        finalidadeEditText = (EditText) findViewById(R.id.finalidadeEditText);
        finalidadeSelectButton = (ImageButton) findViewById(R.id.finalidadeSelectButton);
        finalidadeSelectButton.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFinalidadeFormToSelect();
            }
        });

        formaPagamentoEditText = (EditText) findViewById(R.id.formaPagamentoEditText);
        formaPagamentoSelectButton = (ImageButton) findViewById(R.id.formaPagamentoSelectButton);
        formaPagamentoSelectButton.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFormaPagamentoFormToSelect();
            }
        });

        contaBancariaEditText = (EditText) findViewById(R.id.contaBancariaEditText);
        contaBancariaSelectButton = (ImageButton) findViewById(R.id.contaBancariaSelectButton);
        contaBancariaSelectButton.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openContaBancariaFormToSelect();
            }
        });

        cartaoCreditoEditText = (EditText) findViewById(R.id.cartaoCreditoEditText);
        cartaoCreditoSelectButton = (ImageButton) findViewById(R.id.cartaoCreditoSelectButton);
        cartaoCreditoSelectButton.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openCartaoCreditoFormToSelect();
            }
        });

        valorEditText = (EditText) findViewById(R.id.valorEditText);

        saveButton = (ImageButton) findViewById( R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (movimentoID > 0) {
            new GetMovement(new GetMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    descricaoEditText.setText(movement.getDescricao());
                    descricaoEditText.setEnabled(true);
                    descricaoEditText.setClickable(true);

                    emissaoEditText.setText(DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
                    emissaoEditText.setClickable(true);

                    vencimentoEditText.setText(DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
                    vencimentoEditText.setClickable(true);

                    toggleButtonCredito.setChecked(movement.getOperacao().equals(Movement.CREDIT));
                    toggleButtonDebito.setChecked(movement.getOperacao().equals(Movement.DEBIT));

                    finalidade = movement.getFinalidade();
                    finalidadeEditText.setText(finalidade != null ? finalidade.getDescricao() : "");

                    formaPagamento = movement.getFormaPagamento();
                    formaPagamentoEditText.setText(formaPagamento != null ? formaPagamento.getDescricao() : "");

                    contaBancaria = movement.getContaBancaria();
                    contaBancariaEditText.setText(contaBancaria != null ? contaBancaria.getDescricao() : "");

                    cartaoCredito = movement.getCartaoCredito();
                    cartaoCreditoEditText.setText(cartaoCredito != null ? cartaoCredito.getDescricao() : "");

                    valorEditText.setText(String.valueOf(movement.getValor()));

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(movimentoID);

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
            toggleButtonDebito.setChecked(true);
        }
    }

    ///=====SELECT FINALIDADE======//
    private void openFinalidadeFormToSelect(){
        Intent intent = new Intent(this, FinalityListActivity.class);
        intent.putExtra("ParentClassSource", this.getClass());
        intent.putExtra(FinalityListActivity.KEY_MODE, FinalityListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FINALIDADE_RETURN);
    }

    ///=====SELECT FORMA PAGAMENTO ======//
    private void openFormaPagamentoFormToSelect(){
        Intent intent = new Intent(this, PaymentFormListActivity.class);
        intent.putExtra("ParentClassSource", this.getClass());
        intent.putExtra(PaymentFormListActivity.KEY_MODE, PaymentFormListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FORMA_PAGAMENTO_RETURN);
    }

    ///=====START: POPULATE CONTA BANCARIA SPINNER======//
    private void openContaBancariaFormToSelect(){
        Intent intent = new Intent(this, BankAccountListActivity.class);
        intent.putExtra("ParentClassSource", this.getClass());
        intent.putExtra(BankAccountListActivity.KEY_MODE, BankAccountListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_CONTA_BANCARIA_RETURN);
    }

    ///=====SELECT CARTAO CREDITO======//
    private void openCartaoCreditoFormToSelect(){
        Intent intent = new Intent(this, CreditCardListActivity.class);
        intent.putExtra("ParentClassSource", this.getClass());
        intent.putExtra(CreditCardListActivity.KEY_MODE, CreditCardListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_CARTAO_CREDITO_RETURN);
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case KEY_SELECT_FORMA_PAGAMENTO_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id           = data.getIntExtra(PaymentFormListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(PaymentFormListActivity.KEY_DESCRIPTION);
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
                    int id           = data.getIntExtra(FinalityListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(FinalityListActivity.KEY_DESCRIPTION);
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

            case KEY_SELECT_CARTAO_CREDITO_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id           = data.getIntExtra(CreditCardListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(CreditCardListActivity.KEY_DESCRIPTION);
                    if (cartaoCredito == null){
                        cartaoCredito = new CreditCard(id, descricao);
                    }else{
                        cartaoCredito.setDescricao(descricao);
                        cartaoCredito.setId(id);
                    }
                    cartaoCreditoEditText.setText(descricao);
                }
                break;
            }

            case KEY_SELECT_CONTA_BANCARIA_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id           = data.getIntExtra(BankAccountListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(BankAccountListActivity.KEY_DESCRIPTION);
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
        m.setFinalidade(finalidade);
        m.setContaBancaria(contaBancaria);
        m.setCartaoCredito(cartaoCredito);
        m.setFormaPagamento(formaPagamento);
        m.setValor(Float.parseFloat(valorEditText.getText() != null && !valorEditText.getText().toString().isEmpty() ?
                                    valorEditText.getText().toString() : "0"));
        m.setOperacao(toggleButtonCredito.isChecked() ? Movement.CREDIT : Movement.DEBIT);
        m.setUsuario(3);
        return m;
    }

}
