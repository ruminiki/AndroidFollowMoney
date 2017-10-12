package br.com.followmoney.activities.movements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.activities.KeyParams;
import br.com.followmoney.activities.bankAccounts.BankAccountListActivity;
import br.com.followmoney.activities.creditCards.CreditCardListActivity;
import br.com.followmoney.activities.finalities.FinalityListActivity;
import br.com.followmoney.components.DatePickerFragment;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.CreditCard;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.domain.PaymentForm;
import br.com.followmoney.util.DateUtil;

public class MovementCreateOrEditActivity extends AbstractFormCreateOrEdit<Movement> {

    private static final int KEY_SELECT_FORMA_PAGAMENTO_RETURN = 0;
    private static final int KEY_SELECT_FINALIDADE_RETURN      = 1;
    private static final int KEY_SELECT_CARTAO_CREDITO_RETURN  = 2;
    private static final int KEY_SELECT_CONTA_BANCARIA_RETURN  = 3;

    EditText     descricaoEditText, valorEditText, finalidadeEditText, selectedPaymentEditText; //cartaoCreditoEditText, formaPagamentoEditText, contaBancariaEditText;
    ToggleButton toggleButtonCreditoDebito;
    Button       emissaoTextButton, vencimentoTextButton, paymentMoneyButton, paymentBankButton, paymentCreditCardButton;

    private Finality finalidade;
    private PaymentForm formaPagamento;
    private CreditCard cartaoCredito;
    private BankAccount contaBancaria;

    DatePickerFragment ClasseDataEmissao    = new DatePickerFragment();
    DatePickerFragment ClasseDataVencimento = new DatePickerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movement_create_or_edit);

        finalidadeEditText = (EditText) findViewById(R.id.finalidadeEditText);
        finalidadeEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFinalidadeFormToSelect();
            }
        });

        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);

        emissaoTextButton = (Button) findViewById(R.id.emissaoTextButton);
        emissaoTextButton.setText(DateUtil.format(Calendar.getInstance(), "dd/MM/yyyy"));
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

        vencimentoTextButton = (Button) findViewById(R.id.vencimentoTextButton);
        vencimentoTextButton.setText(DateUtil.format(Calendar.getInstance(), "dd/MM/yyyy"));
        vencimentoTextButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                ClasseDataVencimento.setOnDateSetListener(new DatePickerFragment.OnDateSetListener(){
                    @Override
                    public void onDateSet(Calendar calendar) {
                        vencimentoTextButton.setText(DateUtil.format(calendar, "dd/MM/yyyy"));
                    }
                });
                ClasseDataVencimento.show(getFragmentManager(),  "datepicker");
            }
        });

        toggleButtonCreditoDebito = (ToggleButton) findViewById(R.id.toggleButtonCreditoDebito);
        toggleButtonCreditoDebito.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
               /* if ( toggleButtonCreditoDebito.isChecked() ){
                    toggleButtonCreditoDebito.setTextColor(Color.BLUE);
                }else{
                    toggleButtonCreditoDebito.setTextColor(Color.RED);
                }*/
            }
        });

        paymentMoneyButton = (Button) findViewById(R.id.paymentMoneyButton);
        paymentMoneyButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
        paymentMoneyButton.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMoneyButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                paymentMoneyButton.setTextColor(getResources().getColor(R.color.defaultColor));

                paymentBankButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentBankButton.setTextColor(getResources().getColor(R.color.colorGray));

                paymentCreditCardButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentCreditCardButton.setTextColor(getResources().getColor(R.color.colorGray));

                openContaBancariaFormToSelect();
            }
        });

        paymentBankButton = (Button) findViewById(R.id.paymentBankButton);
        paymentBankButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
        paymentBankButton.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBankButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                paymentBankButton.setTextColor(getResources().getColor(R.color.defaultColor));

                paymentMoneyButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentMoneyButton.setTextColor(getResources().getColor(R.color.colorGray));

                paymentCreditCardButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentCreditCardButton.setTextColor(getResources().getColor(R.color.colorGray));

                openContaBancariaFormToSelect();
            }
        });

        paymentCreditCardButton = (Button) findViewById(R.id.paymentCreditCardButton);
        paymentCreditCardButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
        paymentCreditCardButton.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentCreditCardButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                paymentCreditCardButton.setTextColor(getResources().getColor(R.color.defaultColor));

                paymentBankButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentBankButton.setTextColor(getResources().getColor(R.color.colorGray));

                paymentMoneyButton.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                paymentMoneyButton.setTextColor(getResources().getColor(R.color.colorGray));

                openCartaoCreditoFormToSelect();
            }
        });

        selectedPaymentEditText = (EditText) findViewById(R.id.selectedPaymentEditText);

/*        formaPagamentoEditText = (EditText) findViewById(R.id.formaPagamentoEditText);
        formaPagamentoEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFormaPagamentoFormToSelect();
            }
        });*/

       /* contaBancariaEditText = (EditText) findViewById(R.id.contaBancariaEditText);
        contaBancariaEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openContaBancariaFormToSelect();
            }
        });

        cartaoCreditoEditText = (EditText) findViewById(R.id.cartaoCreditoEditText);
        cartaoCreditoEditText.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                openCartaoCreditoFormToSelect();
            }
        });*/

        valorEditText = (EditText) findViewById(R.id.valorEditText);

        if ( entityID <= 0 ){
            toggleButtonCreditoDebito.setChecked(false);
        }

        super.onCreate(savedInstanceState);
    }

    ///=====SELECT FINALIDADE======//
    private void openFinalidadeFormToSelect(){
        Intent intent = new Intent(this, FinalityListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(FinalityListActivity.KEY_MODE, FinalityListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FINALIDADE_RETURN);
    }

    ///=====SELECT FORMA PAGAMENTO ======//
/*
    private void openFormaPagamentoFormToSelect(){
        Intent intent = new Intent(this, PaymentFormListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(PaymentFormListActivity.KEY_MODE, PaymentFormListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_FORMA_PAGAMENTO_RETURN);
    }
*/

    ///=====START: POPULATE CONTA BANCARIA SPINNER======//
    private void openContaBancariaFormToSelect(){
        Intent intent = new Intent(this, BankAccountListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(BankAccountListActivity.KEY_MODE, BankAccountListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_CONTA_BANCARIA_RETURN);
    }

    ///=====SELECT CARTAO CREDITO======//
    private void openCartaoCreditoFormToSelect(){
        Intent intent = new Intent(this, CreditCardListActivity.class);
        intent.putExtra("ParentActivityName", this.getClass());
        intent.putExtra(CreditCardListActivity.KEY_MODE, CreditCardListActivity.OPEN_TO_SELECT_MODE);
        startActivityForResult(intent, KEY_SELECT_CARTAO_CREDITO_RETURN);
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            /*case KEY_SELECT_FORMA_PAGAMENTO_RETURN :{
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra(PaymentFormListActivity.KEY_ID, 0);
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
            }*/

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
                    int id                  = data.getIntExtra(CreditCardListActivity.KEY_ID, 0);
                    String descricao        = data.getStringExtra(CreditCardListActivity.KEY_DESCRIPTION);
                    int diaFechamentoFatura = data.getIntExtra(KeyParams.KEY_INVOICE_DAY_CLOSING, 0);
                    int diaVencimentoFatura = data.getIntExtra(KeyParams.KEY_INVOICE_DAY_MATURITY, 0);

                    if (cartaoCredito == null){
                        cartaoCredito = new CreditCard(id, descricao);
                    }else{
                        cartaoCredito.setDescricao(descricao);
                        cartaoCredito.setId(id);
                    }
                    contaBancaria = null;
                    if ( diaFechamentoFatura > 0 && diaVencimentoFatura > 0 ){
                        Calendar dataVencimento = DateUtil.getVencimentoMovimentoCartaoCredito(diaVencimentoFatura, diaFechamentoFatura);
                        vencimentoTextButton.setText(DateUtil.format(dataVencimento,"dd/MM/yyyy"));
                        ClasseDataVencimento.setDate(dataVencimento);
                    }
                    selectedPaymentEditText.setText(descricao);
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
                    cartaoCredito = null;
                    selectedPaymentEditText.setText(descricao);
                }
                break;
            }
        }
    }

    @Override
    protected void entityToEditLoaded(Movement movement) {
        if ( movement != null ){
            descricaoEditText.setText(movement.getDescricao());
            descricaoEditText.setEnabled(true);
            descricaoEditText.setClickable(true);

            emissaoTextButton.setText(DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
            ClasseDataEmissao.setDate(DateUtil.toCalendar(movement.getEmissao(), "yyyyMMdd"));
            vencimentoTextButton.setText(DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
            ClasseDataVencimento.setDate(DateUtil.toCalendar(movement.getVencimento(), "yyyyMMdd"));

            toggleButtonCreditoDebito.setChecked(movement.getOperacao().equals(Movement.CREDIT));

            finalidade = movement.getFinality();
            finalidadeEditText.setText(finalidade != null ? finalidade.getDescricao() : "");

            /*formaPagamento = movement.getPaymentForm();
            formaPagamentoEditText.setText(formaPagamento != null ? formaPagamento.getDescricao() : "");*/

            contaBancaria = movement.getBankAccount();
            cartaoCredito = movement.getCreditCard();

            selectedPaymentEditText.setText(contaBancaria != null ? contaBancaria.getDescricao() :
                                           (cartaoCredito != null ? cartaoCredito.getDescricao() : ""));

            valorEditText.setText(String.valueOf(movement.getValor()));
        }

    }

    @Override
    protected Movement getValueDataFieldsInView(Integer id) {
        Movement m = new Movement();
        m.setId(id);
        m.setDescricao(descricaoEditText.getText().toString());
        m.setEmissao(DateUtil.format(ClasseDataEmissao.getDate(), "yyyyMMdd"));
        m.setVencimento(DateUtil.format(ClasseDataVencimento.getDate(), "yyyyMMdd"));
        m.setFinality((finalidade != null && finalidade.getId() != null) ? finalidade : null);
        m.setBankAccount((contaBancaria != null && contaBancaria.getId() != null) ? contaBancaria : null);
        m.setCreditCard((cartaoCredito != null && cartaoCredito.getId() != null) ? cartaoCredito : null);
        /*m.setPaymentForm((formaPagamento != null && formaPagamento.getId() != null) ? formaPagamento : null);*/
        m.setValor(Float.parseFloat(valorEditText.getText() != null && !valorEditText.getText().toString().isEmpty() ?
                valorEditText.getText().toString() : "0"));
        m.setOperacao(toggleButtonCreditoDebito.isChecked() ? Movement.CREDIT : Movement.DEBIT);
        m.setStatus("PAGO");
        m.setUsuario(3);
        return m;
    }

    @Override
    protected String getRestContextGetOrPut() {
        return "/movements/"+entityID;
    }

    @Override
    protected String getRestContextPost() {
        return "/movements";
    }

    @Override
    protected Class getActivityClassList() {
        return MovementListActivity.class;
    }

    @Override
    protected Type getType() {
        return new TypeToken<Movement>(){}.getType();
    }

}
