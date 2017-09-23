package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.activities.movements.MovementDetailActivity;
import br.com.followmoney.domain.Movement;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;

public class BankAccountExtractMovementListActivity extends AbstractFormList<Movement>{

    int bankAccountID = 0;
    String bankAccountDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_extract_movement_list);

        bankAccountID          = getIntent().getIntExtra(KEY_EXTRA_BANK_ACCOUNT_ID, 0);
        bankAccountDescription = getIntent().getStringExtra(KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION);

        TextView bankAccountDescriptionTextView = (TextView) findViewById(R.id.bankAccountDescriptionTextView);
        bankAccountDescriptionTextView.setText(bankAccountDescription);

        TextView atualBalanceTextView = (TextView) findViewById(R.id.atualBalanceTextView);
        atualBalanceTextView.setText("Saldo Atual: R$ 0,00");//@TODO buscar o saldo da conta bancaria no servidor

        TextView foreseenBalanceTextView = (TextView) findViewById(R.id.foreseenBalanceTextView);
        foreseenBalanceTextView.setText("Previsto: R$ 0,00");//@TODO buscar o saldo da conta bancaria no servidor

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        listView.setAdapter(new CustomListAdapter<Movement>(this, R.layout.extract_movement_list_renderer, movements));
    }

    @Override
    protected String getRestContextList() {
        return "/movements/extract/"+bankAccountID+"/period/201708";
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
