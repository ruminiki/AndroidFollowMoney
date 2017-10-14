package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.activities.KeyParams;
import br.com.followmoney.activities.creditCards.CreditCardListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.components.DatePickerFragment;
import br.com.followmoney.domain.AccountTransfer;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.globals.GlobalParams;
import br.com.followmoney.util.DateUtil;

public class BankAccountTransfer extends AbstractFormCreateOrEdit<AccountTransfer>{

    private static final int KEY_SELECT_FINALIDADE_RETURN              = 1;
    private static final int KEY_SELECT_CONTA_BANCARIA_ORIGEM_RETURN   = 2;
    private static final int KEY_SELECT_CONTA_BANCARIA_DESTINO_RETURN  = 3;

    EditText descricaoEditText, valorEditText, finalidadeEditText, contaBancariaOrigemEditText, contaBancariaDestinoEditText;
    DatePickerFragment ClasseDataEmissao = new DatePickerFragment();
    Button emissaoTextButton;
    private Finality finalidade;
    private BankAccount contaBancariaOrigem, contaBancariaDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_transfer);

        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        valorEditText = (EditText) findViewById(R.id.valorEditText);

        emissaoTextButton = (Button) findViewById(R.id.emissaoTextButton);
        emissaoTextButton.setText(DateUtil.format(Calendar.getInstance(), "dd/MM/yyyy"));
        ClasseDataEmissao.setDate(Calendar.getInstance());
        emissaoTextButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                ClasseDataEmissao.setOnDateSetListener(new DatePickerFragment.OnDateSetListener(){
                    @Override
                    public void onDateSet(Calendar calendar) {
                        emissaoTextButton.setText(DateUtil.format(calendar, "dd/MM/yyyy"));
                    }
                });
                ClasseDataEmissao.show(getFragmentManager(),  "datepicker");
            }
        });

        finalidadeEditText = (EditText) findViewById(R.id.finalidadeEditText);
        finalidadeEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFinalidadeFormToSelect();
            }
        });

        contaBancariaOrigemEditText = (EditText) findViewById(R.id.contaBancariaOrigemEditText);
        contaBancariaOrigemEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openContaBancariaFormToSelect(KEY_SELECT_CONTA_BANCARIA_ORIGEM_RETURN);
            }
        });

        contaBancariaDestinoEditText = (EditText) findViewById(R.id.contaBancariaDestinoEditText);
        contaBancariaDestinoEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openContaBancariaFormToSelect(KEY_SELECT_CONTA_BANCARIA_DESTINO_RETURN);
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityToEditLoaded(AccountTransfer entity) {}

    ///=====SELECT FINALIDADE======//
    private void openFinalidadeFormToSelect(){
        Intent intent = new Intent(this, FinalityListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass().getName());
        intent.putExtra(FinalityListActivity.KEY_MODE, FinalityListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FINALIDADE_RETURN);
    }

    ///=====START: POPULATE CONTA BANCARIA SPINNER======//
    private void openContaBancariaFormToSelect(int key){
        Intent intent = new Intent(this, BankAccountListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass().getName());
        intent.putExtra(BankAccountListActivity.KEY_MODE, BankAccountListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, key);
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
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

            case KEY_SELECT_CONTA_BANCARIA_ORIGEM_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra(BankAccountListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(BankAccountListActivity.KEY_DESCRIPTION);
                    String tipo = data.getStringExtra(KeyParams.KEY_EXTRA_BANK_ACCOUNT_TYPE);
                    String status = data.getStringExtra(KeyParams.KEY_EXTRA_BANK_ACCOUNT_STATUS);

                    if (contaBancariaOrigem == null) {
                        contaBancariaOrigem = new BankAccount();
                    }
                    contaBancariaOrigem.setId(id);
                    contaBancariaOrigem.setDescricao(descricao);
                    contaBancariaOrigem.setTipo(tipo);
                    contaBancariaOrigem.setSituacao(status);

                    contaBancariaOrigemEditText.setText(contaBancariaOrigem.getDescricao());
                }
                break;
            }

            case KEY_SELECT_CONTA_BANCARIA_DESTINO_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra(BankAccountListActivity.KEY_ID, 0);
                    String descricao = data.getStringExtra(BankAccountListActivity.KEY_DESCRIPTION);
                    String tipo = data.getStringExtra(KeyParams.KEY_EXTRA_BANK_ACCOUNT_TYPE);
                    String status = data.getStringExtra(KeyParams.KEY_EXTRA_BANK_ACCOUNT_STATUS);

                    if (contaBancariaDestino == null) {
                        contaBancariaDestino = new BankAccount();
                    }
                    contaBancariaDestino.setId(id);
                    contaBancariaDestino.setDescricao(descricao);
                    contaBancariaDestino.setTipo(tipo);
                    contaBancariaDestino.setSituacao(status);

                    contaBancariaDestinoEditText.setText(contaBancariaDestino.getDescricao());
                }
                break;
            }
        }
    }

    @Override
    protected boolean validateSave() {
        if ( contaBancariaOrigem != null && contaBancariaDestino != null ){
            if ( contaBancariaOrigem.getId() == contaBancariaDestino.getId() ){
                Toast.makeText(getApplicationContext(), "A conta de origem e destino devem ser diferentes. Tente novamente.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected AccountTransfer getValueDataFieldsInView(Integer id) {
        AccountTransfer a = new AccountTransfer();
        a.setData(DateUtil.format(ClasseDataEmissao.getDate(), "yyyyMMdd"));
        a.setFinalidade((finalidade != null && finalidade.getId() != null) ? finalidade : null);
        a.setContaBancariaOrigem((contaBancariaOrigem != null && contaBancariaOrigem.getId() != null) ? contaBancariaOrigem : null);
        a.setContaBancariaDestino((contaBancariaDestino != null && contaBancariaDestino.getId() != null) ? contaBancariaDestino : null);
        a.setValor(Float.parseFloat(valorEditText.getText() != null && !valorEditText.getText().toString().isEmpty() ?
                valorEditText.getText().toString() : "0"));
        a.setUsuario(GlobalParams.getInstance().getUserOnLineID());
        return a;
    }

    @Override
    protected String getRestContextGetOrPut() {
        return null;
    }

    @Override
    protected String getRestContextPost() {
        return "/movements/accountTransfer";
    }

    @Override
    protected Class getActivityClassList() {
        return BankAccountListActivity.class;
    }

    @Override
    protected Type getType() {
        return new TypeToken<BankAccount>(){}.getType();
    }

}
