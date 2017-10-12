package br.com.followmoney.activities.bankAccounts;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.domain.BankAccount;

public class BankAccountCreateOrEditActivity extends AbstractFormCreateOrEdit<BankAccount>{

    EditText descricaoEditText, numeroEditText, digitoEditText;
    ToggleButton toggleButtonType, toggleButtonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_create_or_edit);

        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        numeroEditText    = (EditText) findViewById(R.id.numeroEditText);
        toggleButtonType = (ToggleButton) findViewById(R.id.toggleButtonType);
        toggleButtonStatus = (ToggleButton) findViewById(R.id.toggleButtonStatus);
        digitoEditText    = (EditText) findViewById(R.id.digitoEditText);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityToEditLoaded(BankAccount bankAccount) {
        if ( bankAccount != null ){
            descricaoEditText.setText(bankAccount.getDescricao());
            descricaoEditText.setEnabled(true);
            descricaoEditText.setClickable(true);

            numeroEditText.setText(String.valueOf(bankAccount.getNumero()));
            numeroEditText.setEnabled(true);
            numeroEditText.setClickable(true);

            toggleButtonType.setChecked(bankAccount.getTipo().equals(BankAccount.BANK));
            toggleButtonStatus.setChecked(bankAccount.getTipo().equals(BankAccount.ACTIVE));

            digitoEditText.setText(String.valueOf(bankAccount.getDigito()));
            digitoEditText.setEnabled(true);
            digitoEditText.setClickable(true);
        }
    }

    @Override
    protected BankAccount getValueDataFieldsInView(Integer id) {
        BankAccount b = new BankAccount();
        b.setId(id);
        b.setDescricao(descricaoEditText.getText().toString());
        b.setNumero(numeroEditText.getText().toString());
        b.setDigito(Integer.parseInt(digitoEditText.getText().toString()));
        b.setSituacao(toggleButtonStatus.isChecked() ? BankAccount.ACTIVE : BankAccount.INACTIVE);
        b.setTipo(toggleButtonType.isChecked() ? BankAccount.BANK : BankAccount.WALLET);
        b.setUsuario(3);
        return b;
    }

    @Override
    protected String getRestContextGetOrPut() {
        return "/bankAccounts/"+entityID;
    }

    @Override
    protected String getRestContextPost() {
        return "/bankAccounts";
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
