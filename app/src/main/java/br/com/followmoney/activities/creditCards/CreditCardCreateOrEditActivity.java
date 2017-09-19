package br.com.followmoney.activities.creditCards;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.domain.CreditCard;

public class CreditCardCreateOrEditActivity extends AbstractFormCreateOrEdit<CreditCard> {

    EditText descricaoEditText, limiteEditText, dataFechamentoFaturaEditText, dataVencimentoFaturaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_credit_card_create_or_edit);

        //====BIND TEXT FIELDS====//
        descricaoEditText            = (EditText) findViewById(R.id.descricaoEditText);
        limiteEditText               = (EditText) findViewById(R.id.limiteEditText);
        dataFechamentoFaturaEditText = (EditText) findViewById(R.id.dataFechamentoFaturaEditText);
        dataVencimentoFaturaEditText = (EditText) findViewById(R.id.dataVencimentoFaturaEditText);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void entityToEditLoaded(CreditCard creditCard) {
        if ( creditCard != null ){
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
    }

    @Override
    protected CreditCard getValueDataFieldsInView(Integer id) {
        CreditCard c = new CreditCard();
        c.setId(id);
        c.setDescricao(descricaoEditText.getText().toString());
        c.setLimite(Float.parseFloat(limiteEditText.getText().toString()));
        c.setDataFatura(Integer.parseInt(dataVencimentoFaturaEditText.getText().toString()));
        c.setDataFechamento(Integer.parseInt(dataFechamentoFaturaEditText.getText().toString()));
        c.setUsuario(3);
        return c;
    }

    @Override
    protected String getRestContext() {
        return "creditCards";
    }

    @Override
    protected Class getActivityClassList() {
        return CreditCardListActivity.class;
    }

    @Override
    protected Type getType() {
        return new TypeToken<CreditCard>(){}.getType();
    }

}
