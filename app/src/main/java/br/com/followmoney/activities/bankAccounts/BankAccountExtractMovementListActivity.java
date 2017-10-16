package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.activities.movements.MovementDetailActivity;
import br.com.followmoney.dao.remote.StringValueRequest;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.globals.GlobalParams;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;

public class BankAccountExtractMovementListActivity extends AbstractFormList<Movement>{

    int bankAccountID = 0;
    String bankAccountDescription;
    YearMonthPickerDialog yearMonthPickerDialog;
    TextView mesReferenciaTextView, previousBalanceTextView, foreseenBalanceTextView;
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_extract_movement_list);

        bankAccountID          = getIntent().getIntExtra(KEY_EXTRA_BANK_ACCOUNT_ID, 0);
        bankAccountDescription = getIntent().getStringExtra(KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION);

        //BIND COMPONENTS VIEW
        TextView bankAccountDescriptionTextView = (TextView) findViewById(R.id.bankAccountDescriptionTextView);
        bankAccountDescriptionTextView.setText(bankAccountDescription);

        mesReferenciaTextView = (TextView) findViewById(R.id.mesReferenciaTextView);
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        mesReferenciaTextView.setPaintFlags(mesReferenciaTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mesReferenciaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearMonthPickerDialog.show();
            }
        });
        previousBalanceTextView = (TextView) findViewById(R.id.previousBalanceTextView);
        foreseenBalanceTextView = (TextView) findViewById(R.id.foreseenBalanceTextView);

        //SELECT MONTH
        yearMonthPickerDialog = new YearMonthPickerDialog(BankAccountExtractMovementListActivity.this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
                GlobalParams.getInstance().setSelectedMonthReference(dateFormat.format(calendar.getTime()));
                mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
                loadList();
            }
        }, R.style.MonthPickerTheme);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(final List<Movement> movements) {
        //GET BALANCE
        String context = "/movements/previousBalance" +
                "/bankAccount/"+ bankAccountID +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                updateListAndResume(movements, result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);

    }

    private void updateListAndResume(List<Movement> movements, String result) {
        double receitas = 0, despesas = 0, saldoPrevisto = 0, saldoAnterior = 0;

        for ( Movement movement : movements ){
            if ( movement.getOperacao().equals(Movement.CREDIT) ){
                receitas += movement.getValor();
            }else{
                despesas += movement.getValor();
            }
        }

        try{
            saldoAnterior = Double.parseDouble(result);
            saldoPrevisto = saldoAnterior + receitas - despesas;
        }catch (Exception e ){
            saldoAnterior = 0;
            saldoPrevisto = saldoAnterior + receitas - despesas;
        }

        previousBalanceTextView.setText(numberFormat.format(saldoAnterior));
        foreseenBalanceTextView.setText(numberFormat.format(saldoPrevisto));

        listView.setAdapter(new CustomListAdapter<Movement>(this, R.layout.extract_movement_list_renderer, movements));
    }

    @Override
    protected String getRestContextList() {
        return "/movements/bankAccount/"+bankAccountID+"/period/"+GlobalParams.getInstance().getSelectedMonthReference();
    }

    @Override
    protected String getRestContextDelete() {
        return null;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) { }

    @Override
    protected Type getType() {
        return new TypeToken<List<Movement>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntity = (Movement) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        Intent intent = new Intent(this, MovementDetailActivity.class);
        intent.putExtra(KEY_EXTRA_MOVEMENT_ID, selectedEntity.getId());
        startActivity(intent);
    }

}
